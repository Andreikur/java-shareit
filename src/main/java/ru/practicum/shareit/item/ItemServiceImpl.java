package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ItemNotCreatedByUserException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, long idUserOwner) {
        Item item = itemRepository.save(ItemMapper.toItem(itemDto));
        //ДОБАВИТЬ СОЗДАТЕЛЯ

        return ItemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto updateItem(long id, Map<String, String> itemData, long idUserOwner) {
        Item currentItem = itemRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь с таким id не найдена")));

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

    /*public List<ItemDto> getAllItemsUser(long idUserOwner) {
        List<Item> itemList = itemStorage.getAllItemsUser(idUserOwner);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }*/

    /*public List<ItemDto> searchItem(long idUserOwner, String text) {
        List<Item> itemList = itemStorage.searchItem(idUserOwner, text);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }*/
}
