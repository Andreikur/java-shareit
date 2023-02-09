package ru.practicum.shareit.item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, long idUserOwner);

    ItemDto updateItem(long id, Map<String, String> itemData, long idUserOwner);

    ItemDto getItem(long id);

    List<ItemDto> getAllItem();

    //List<ItemDto> getAllItemsUser(long idUserOwner);

    //List<ItemDto> searchItem(long idUserOwner, String text);

}
