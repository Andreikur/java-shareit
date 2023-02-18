package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, long idUserOwner);

    ItemDto updateItem(long id, Map<String, String> itemData, long idUserOwner);

    ItemDto getItem(long id);

    List<ItemDto> getAllItem();

    List<ItemDto> getAllItemsUser(long idUserOwner);

    List<ItemDto> searchItem(long idUserOwner, String text);
    CommentDto addComment(long userId, long itemId, CommentDto commentDto);

}
