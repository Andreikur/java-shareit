package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ItemNotCreatedByUserException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemoryItemStorage implements ItemStorage {

    private long id;
    private final Map<Long, Item> allItem = new HashMap<>();
    private final UserStorage userStorage;

    public Item addItem(Item item, long idUserOwner){
        userStorage.getUser(idUserOwner);
        item.setOwner(idUserOwner);
        id++;
        item.setId(id);
        allItem.put(id, item);
        log.info("Вещь добавлена");
        return allItem.get(id);
    }

    public Item updateItem(long id, Map<String, String> itemData, long idUserOwner){
        checkItem(id);
        Item currentItem = allItem.get(id);
        if (currentItem.getOwner() != idUserOwner){
            log.info("Вещь создана другим пользователем");
            throw new ItemNotCreatedByUserException(String.format(
                    "Вещь создана другим пользователем"));
        }
        if(itemData.containsKey("name")) {
            currentItem.setName(itemData.get("name"));
        }
        if(itemData.containsKey("description")) {
            currentItem.setDescription(itemData.get("description"));
        }
        if(itemData.containsKey("available")) {
            currentItem.setAvailable(Boolean.parseBoolean(itemData.get("available")));
        }
        return currentItem;
    }

    /**
     * Получаем вещь по Id
     * @param id
     * @return
     */
    public Item getItem(long id){
        checkItem(id);
        return allItem.get(id);
    }

    /**
     * Получаем список всех вещей
     * @return
     */
    public List<Item> getAllItem(){
        return List.copyOf(allItem.values());
    }

    /**
     * Получаем список всех вещей пользователя
     * @param idUserOwner
     * @return
     */
    public List<Item> getAllItemsUser(long idUserOwner){
        List<Item> itemList = new ArrayList<>();
        for (Item curItem : allItem.values()){
            if(curItem.getOwner() == idUserOwner){
                itemList.add(curItem);
            }
        }
        return itemList;
    }

    public List<Item> searchItem(long idUserOwner, String text){
        List<Item> itemList = new ArrayList<>();
        if(text.isBlank()){
            return itemList;
        }
        for (Item curItem : allItem.values()){
            if(curItem.getDescription().toLowerCase().contains(text) && curItem.getAvailable()){
                itemList.add(curItem);
            }
        }
        return itemList;
    }

    /**
     * Проверка вещи на наличие, если отсутствует выбрасывается исключение
     * @param id
     */
    private void checkItem(Long id){
        if(!allItem.containsKey(id)){
            log.info("Вещь с таким id отсутствует в базе");
            throw new ItemNotFoundException(String.format(
                    "Вещь с id отсутствует в базе", id));
        }
    }

}
