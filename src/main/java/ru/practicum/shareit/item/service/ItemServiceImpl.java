package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.CommentRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, long idUserOwner) {

        User user = userRepository.findById(idUserOwner).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));


        itemDto.setOwner(user);

        Item item = itemRepository.save(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto updateItem(long id, Map<String, String> itemData, long idUserOwner) {
        Item currentItem = itemRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь с таким id не найдена")));

        if (currentItem.getOwner().getId() != idUserOwner){
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
    public ItemDto getItem(long id) {
        try {
            Item item = itemRepository.findById(id).orElseThrow(() ->
                    new ItemNotFoundException(String.format("Вещь с таким id не найден")));
            return ItemMapper.toItemDto(item);
            //return setComments(ItemMapper.toItemDto(item));
        } catch (DataIntegrityViolationException e){
            if(e.getCause() instanceof ConstraintViolationException) {
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
    public List<ItemDto> getAllItemsUser(long idUserOwner) {
        List<Item> itemList = itemRepository.findAll();
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            if (item.getOwner().getId() == idUserOwner) {
                itemDtoList.add(ItemMapper.toItemDto(item));
            }
        }
        return itemDtoList;
    }

    public List<ItemDto> searchItem(long idUserOwner, String text) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        if (text.isBlank()) {
            return itemDtoList;
        }
        List<Item> itemList = itemRepository.findAll();
        for (Item curItem : itemList) {
            if(curItem.getDescription().toLowerCase().contains(text) && curItem.getAvailable()) {
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
                            itemBooking.getId(), LocalDateTime.now(),userId
                    ).map(BookingMapper::toBookingDto).orElse(null));
            itemBooking.setNextBooking(
                    bookingRepository.findNextBooking(
                            itemBooking.getId(), LocalDateTime.now(),userId
                    ).map(BookingMapper::toBookingDto).orElse(null));
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
                .orElseThrow(() -> new BookingNotFoundException("Бронирование отсутствует"));

        /*Booking booking = bookingRepository.findById(idBooking).orElseThrow(() ->
                new BookingNotFoundException(String.format("Бронь с таким id не найдена")));*/

        Comment comment = CommentMapper.toComment(user, item, commentDto);
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }
}
