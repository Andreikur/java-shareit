package ru.practicum.shareit.item;

import java.util.List;
import java.util.Map;

public interface ItemStorage {
    Item addItem(Item item, long idUserOwner);

    Item updateItem(long id, Map<String, String> itemData, long idUserOwner);

    Item getItem(long id);

    List<Item> getAllItem();

    List<Item> getAllItemsUser(long idUserOwner);

    List<Item> searchItem(long idUserOwner, String text);

}
