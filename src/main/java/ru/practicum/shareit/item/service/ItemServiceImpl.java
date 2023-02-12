package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ItemNotCreatedByUserException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, long idUserOwner) {
        /*if(userRepository.findById(idUserOwner).isEmpty()) {
            log.info("Пользователь с таким id отсутствует в базе");
            throw new UserNotFoundException(String.format(
                    "Пользователь с id отсутствует в базе"));
        }*/

        UserDto userDto = userService.getUser(idUserOwner);
        itemDto.setOwner(UserMapper.toUser(userDto));

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
}
