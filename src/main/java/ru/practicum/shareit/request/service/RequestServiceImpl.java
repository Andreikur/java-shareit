package ru.practicum.shareit.request.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShort;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));

        itemRequestDto.setRequestor(user);
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = requestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto));
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestShort> getAllYourItemRequestDto(long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.
                toItemRequestDto(requestRepository.findAllItemRequestByReguestor(userId));

        this.setItemsToItemRequestDto(itemRequestDtoList);

        return ItemRequestMapper.toItemRequestShort(itemRequestDtoList);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestShort> getAllOthersItemRequestDto(long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.
                toItemRequestDto(requestRepository.findAllItemRequestCreatedByOthers(userId));

        return ItemRequestMapper.toItemRequestShort(itemRequestDtoList);
    }

    /**
     * Получить список запросов созданных другими пользователями
     * @param userId
     * @param from
     * @param size
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestShort> getAllOthersItemRequestDtoPageByPage(long userId, long from, long size) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        List<ItemRequest> itemRequests = requestRepository.findAllItemRequestCreatedByOthers(userId);

        List<ItemRequest> itemRequestsSort = new ArrayList<>();
        itemRequestsSort.addAll((int) from, itemRequests);
        if (itemRequestsSort.size() < size) {
            size = itemRequestsSort.size();
        }
        itemRequests.clear();
        for (int i = 0; i < size; i++) {
            itemRequests.add(itemRequestsSort.get(i));
        }

        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.toItemRequestDto(itemRequests);
        this.setItemsToItemRequestDto(itemRequestDtoList);

        return ItemRequestMapper.toItemRequestShort(itemRequestDtoList);
    }

    /**
     * Получить данные о конкретном запросе
     * @param userId
     * @param requestId
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public ItemRequestShort getItemRequest(long userId, long requestId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        requestRepository.findById(requestId).orElseThrow(() ->
                new UserNotFoundException(String.format("Запрос с таким id не найден")));
        ItemRequest itemRequest = requestRepository.findItemRequest(requestId);

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        this.setItemsToItemRequestDto(List.of(itemRequestDto));
        return ItemRequestMapper.toItemRequestShort(itemRequestDto);
    }

    private List<ItemRequestDto> setItemsToItemRequestDto(List<ItemRequestDto> itemRequestDtoList){
        for (ItemRequestDto itemRequestDto : itemRequestDtoList){
            List<Item> itemList = itemRepository.findAllThisItemRequest(ItemRequestMapper.toItemRequest(itemRequestDto));
            itemRequestDto.setItems(itemList);
        }
        return itemRequestDtoList;
    }
}
