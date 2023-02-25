package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, long idUserOwner) {
        User user = userRepository.findById(idUserOwner).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        itemDto.setOwner(user);

        Item item = ItemMapper.toItem(itemDto);
        if(itemDto.getRequestId() != null){
            ItemRequest itemRequest = requestRepository.findById(itemDto.getRequestId()).orElseThrow(() ->
                    new UserNotFoundException(String.format("Запрос с таким id не найден")));
            item.setRequest(itemRequest);

            List<Item> itemSet = itemRequest.getItems();

            System.out.println(itemSet);  //!!!!!!!!!!!!!!!!!!!!!!!!!!
            itemSet.add(item);
            System.out.println(itemSet);  //!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto updateItem(long id, Map<String, String> itemData, long idUserOwner) {
        Item currentItem = itemRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь с таким id не найдена")));
        if (currentItem.getOwner().getId() != idUserOwner) {
            log.info("Вещь создана другим пользователем");
            throw new ItemNotCreatedByUserException(String.format(
                    "Вещь создана другим пользователем"));
        }
        if (itemData.containsKey("name")) {
            currentItem.setName(itemData.get("name"));
        }
        if (itemData.containsKey("description")) {
            currentItem.setDescription(itemData.get("description"));
        }
        if (itemData.containsKey("available")) {
            currentItem.setAvailable(Boolean.parseBoolean(itemData.get("available")));
        }
        return ItemMapper.toItemDto(currentItem);
    }

    @Transactional(readOnly = true)
    public ItemBooking getItem(long userId, long itemId) {
        try {
            Item item = itemRepository.findById(itemId).orElseThrow(() ->
                    new ItemNotFoundException(String.format("Вещь с таким id не найден")));
            return setComments(setBookings(userId, item), itemId);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new ItemNotCreatedByUserException(String.format(
                        "Вещь с таким id не найдена"));
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getAllItem() {
        List<Item> items = itemRepository.findAll();
        return ItemMapper.toItemDto(items);
    }

    @Transactional(readOnly = true)
    public List<ItemBooking> getAllItemsUser(long idUserOwner) {
        return itemRepository.findAllByOwnerIdOrderByIdAsc(idUserOwner).stream()
                .map(item -> setBookings(idUserOwner, item))
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItem(long idUserOwner, String text) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        if (text.isBlank()) {
            return itemDtoList;
        }
        List<Item> itemList = itemRepository.findAll();
        for (Item curItem : itemList) {
            if (curItem.getDescription().toLowerCase().contains(text) && curItem.getAvailable()) {
                itemDtoList.add(ItemMapper.toItemDto(curItem));
            }
        }
        return itemDtoList;
    }

    private ItemBooking setBookings(long userId, Item item) {
        ItemBooking itemBooking = ItemMapper.toItemBooking(item);
        if (item.getOwner().getId() == userId) {
            itemBooking.setLastBooking(
                    bookingRepository.findLastBooking(
                            item.getId(), LocalDateTime.now(), userId)
                            .map(BookingMapper::toBookingDtoShort).orElse(null));
            itemBooking.setNextBooking(
                    bookingRepository.findNextBooking(
                            item.getId(), LocalDateTime.now(), userId)
                            .map(BookingMapper::toBookingDtoShort).orElse(null));
        } else {
            itemBooking.setLastBooking(null);
            itemBooking.setNextBooking(null);
        }
        return itemBooking;
    }

    private ItemBooking setComments(ItemBooking itemDtoBooking, long itemId) {
        List<CommentDto> commentList = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        itemDtoBooking.setComments(commentList);
        return itemDtoBooking;
    }

    @Override
    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь с таким id не найден")));

        bookingRepository.findByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new BookingBadRequestException("Бронирование отсутствует"));
        Comment comment = CommentMapper.toComment(user, item, commentDto);
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }
}
