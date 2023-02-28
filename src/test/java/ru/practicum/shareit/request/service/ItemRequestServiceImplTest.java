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
    private ItemRepository itemRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Item item;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;



    @BeforeEach
    void beforeEach() {
        LocalDateTime data = LocalDateTime.now();
        List<Item> items = new ArrayList<>();
        user = new User(1L, "user1@mail.com", "User1");
        item = new Item(1L,"name","description",true, user,null);

        itemRequest = new ItemRequest(1, "ItemRequest_description", user, data, items);

        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        Item item = new Item(1, "Item1", "Item_description", null, user, itemRequest);

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
    void createWhenUserNotFoundExceptionThrownTest() {
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

    /*@Test
    void getAllOthersItemRequestDtoPageByPageTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        item.setRequest(itemRequest);

        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong())).thenReturn(Collections.singletonList(item));

        ItemRequestDto responseRequest = requestService.getAllOthersItemRequestDtoPageByPage(user.getId(), itemRequestDto.getId());

        assertNotNull(responseRequest);
        verify(requestRepository).findById(anyLong());
        verify(itemRepository).findByItemRequestId(anyLong());
    }*/

    /*@Test
    void getRequestInfo_whenRequestNotFound_thenExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(requestRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException("Request not found"));

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () ->
                requestService.getRequestInfo(user.getId(), itemRequestDto.getId()));
        assertEquals("Request not found", exception.getMessage());
    }*/

    /*@Test
    void getRequestsListTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(requestRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException("Запрос не найден"));

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () ->
                requestService.getRequestInfo(user.getId(), 1L)
        );

        assertEquals("Запрос не найден", exception.getMessage());
    }*/

    /*@Test
    void getItemRequestsTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        when(requestRepository.findAllPageable(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));

        List<ItemRequestDtoResponse> itemRequestDtos = requestService.getRequestsList(
                user.getId(),
                0,
                10);

        assertEquals(1, itemRequestDtos.size());
        assertEquals(1, itemRequestDtos.get(0).getId());
        assertEquals("description", itemRequestDtos.get(0).getDescription());
        assertEquals(user.getId(), itemRequestDtos.get(0).getRequestorId());
        assertEquals(Collections.emptyList(), itemRequestDtos.get(0).getItems());
    }*/
}
