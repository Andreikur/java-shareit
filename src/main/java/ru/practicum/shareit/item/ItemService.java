package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;

    public Item addItem(Item item, long idUserOwner) {
        return itemStorage.addItem(item, idUserOwner);
    }

    public Item updateItem(long id, Map<String, String> itemDAta, long idUserOwner) {
        return itemStorage.updateItem(id, itemDAta, idUserOwner);
    }

    public ItemDto getItem(long id) {
        return ItemMapper.toItemDto(itemStorage.getItem(id));
    }

    public List<ItemDto> getAllItem() {
        List<Item> itemList = itemStorage.getAllItem();
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    public List<ItemDto> getAllItemsUser(long idUserOwner) {
        List<Item> itemList = itemStorage.getAllItemsUser(idUserOwner);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    public List<ItemDto> searchItem(long idUserOwner, String text) {
        List<Item> itemList = itemStorage.searchItem(idUserOwner, text);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }
}
