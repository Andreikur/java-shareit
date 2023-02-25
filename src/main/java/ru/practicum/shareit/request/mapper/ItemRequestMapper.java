package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShort;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated(),
                itemRequest.getItems()
        );
    }

    public static List<ItemRequestDto> toItemRequestDto(Iterable<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestDtoList.add(toItemRequestDto(itemRequest));
        }
        return itemRequestDtoList;
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getRequestor(),
                itemRequestDto.getCreated(),
                itemRequestDto.getItems()
        );
    }

    public static ItemRequestShort toItemRequestShort(ItemRequest itemRequest) {
        List<Long> listIdItems = new ArrayList<>();
        for (Item item : itemRequest.getItems()) {
            listIdItems.add(item.getId());
        }
        return new ItemRequestShort(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated(),
                ItemMapper.toItemDto(itemRequest.getItems())
        );
    }

    public static List<ItemRequestShort> toItemRequestShort(Iterable<ItemRequest> itemRequests) {
        List<ItemRequestShort> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestDtoList.add(toItemRequestShort(itemRequest));
        }
        return itemRequestDtoList;
    }

}
