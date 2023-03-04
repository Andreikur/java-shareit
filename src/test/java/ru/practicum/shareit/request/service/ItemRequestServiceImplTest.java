package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemRequestServiceImplTest {

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private User user2;
    LocalDateTime data;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void beforeEach() {
        data = LocalDateTime.now();
        List<Item> items = new ArrayList<>();
        user = new User(1L, "user1@mail.com", "User1");
        user2 = new User(2L, "user2@mail.com", "User2");
        Item item = new Item(1, "Item1", "Item_description", null, user, itemRequest);

        itemRequest = new ItemRequest(1, "ItemRequest_description", user, data);

        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        itemRequestDto.setItems(List.of(item));
    }


    @Test
    void addRequestTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        when(requestRepository.save(any())).thenReturn(itemRequest);

        ItemRequestDto actual = requestService.addRequest(itemRequestDto, user.getId());

        itemRequestDto.setCreated(actual.getCreated());

        assertEquals(itemRequestDto.getId(), actual.getId());
        verify(requestRepository, Mockito.times(1)).save(any());
    }

    @Test
    void addRequestWhenUserNotFoundExceptionThrownTest() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь с таким id не найден"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> requestService.addRequest(itemRequestDto, 1));

        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void getAllYourItemRequestDtoTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        List<ItemRequestShort> itemRequestList = requestService.getAllYourItemRequestDto(user.getId());
        assertTrue(itemRequestList.isEmpty());
        verify(requestRepository).findAllItemRequestByReguestor(anyLong());
    }

    @Test
    void getAllYourItemRequestDtoWhenUserNotFoundTest() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь с таким id не найден"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> requestService.getAllYourItemRequestDto(1));
        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void getAllOthersItemRequestDtoTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user2));

        List<ItemRequestShort> itemRequestList = requestService.getAllOthersItemRequestDto(user2.getId());
        assertTrue(itemRequestList.isEmpty());
        verify(requestRepository).findAllItemRequestCreatedByOthers(anyLong());
    }

    @Test
    void getAllOthersItemRequestDtoWhenUserNotFoundTest() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь с таким id не найден"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> requestService.getAllOthersItemRequestDto(2));
        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void getAllOthersItemRequestDtoPageByPageTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        List<ItemRequestShort> itemRequestList = requestService.getAllOthersItemRequestDtoPageByPage(user.getId(), 0, 1);
        assertTrue(itemRequestList.isEmpty());
        verify(requestRepository).findAllItemRequestCreatedByOthers(anyLong());
    }

    @Test
    void getAllOthersItemRequestDtoPageByPageWhenUserNotFoundTest() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь с таким id не найден"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> requestService.getAllOthersItemRequestDtoPageByPage(1, 0, 1));
        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void getItemRequestUserNotFoundTest() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь с таким id не найден"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> requestService.getItemRequest(1, 1));
        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void getItemRequestRequestNotFoundTest() {
        when(requestRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Запрос с таким id не найден"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> requestService.getItemRequest(1, 1));
        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }
}
