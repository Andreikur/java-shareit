package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBooking;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, long idUserOwner);

    ItemDto updateItem(long id, Map<String, String> itemData, long idUserOwner);

    ItemBooking getItem(long userId, long itemId);

    List<ItemDto> getAllItem();

    List<ItemBooking> getAllItemsUser(long idUserOwner);

    List<ItemDto> searchItem(long idUserOwner, String text);
    CommentDto addComment(long userId, long itemId, CommentDto commentDto);

}
