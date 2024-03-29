package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShort;

import java.util.List;

public interface RequestService {

    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestShort> getAllYourItemRequest(long userId);

    List<ItemRequestShort> getAllOthersItemRequest(long userId);

    List<ItemRequestShort> getAllOthersItemRequestDtoPageByPage(long userId, long from, long size);

    ItemRequestShort getItemRequest(long userId, long requestId);
}
