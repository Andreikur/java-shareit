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
                new ArrayList<>()
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
                itemRequestDto.getCreated()
                //itemRequestDto.getItems()
        );
    }

    public static ItemRequestShort toItemRequestShort(ItemRequestDto itemRequestDto) {
        List<Long> listIdItems = new ArrayList<>();
        for (Item item : itemRequestDto.getItems()) {
            listIdItems.add(item.getId());
        }
        return new ItemRequestShort(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getRequestor(),
                itemRequestDto.getCreated(),
                ItemMapper.toItemDto(itemRequestDto.getItems())
        );
    }

    public static List<ItemRequestShort> toItemRequestShort(Iterable<ItemRequestDto> itemRequestDtos) {
        List<ItemRequestShort> itemRequestShortsList = new ArrayList<>();
        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            itemRequestShortsList.add(toItemRequestShort(itemRequestDto));
        }
        return itemRequestShortsList;
    }

}
