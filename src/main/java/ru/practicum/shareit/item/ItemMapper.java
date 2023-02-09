package ru.practicum.shareit.item;

import java.util.ArrayList;
import java.util.List;

public final class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
                //item.getOwner(),
                //item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static List<ItemDto> toItemDto(Iterable<Item> items){
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : items){
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
                //itemDto.getOwner(),
                //itemDto.getRequest() != null ? itemDto.getRequest() : null
        );
    }
}
