package ru.practicum.shareit.request.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

import ru.practicum.shareit.request.dto.ItemRequestShort;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static ru.practicum.shareit.ShareItApp.HEADER;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerTest {

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper mapper;
    ItemRequestDto itemRequestDto;
    User user;

    @BeforeEach
    void beforeEach() {
        List<Item> items = new ArrayList<>();
        user = new User(1L, "user1@mail.com", "User1");

        itemRequestDto = new ItemRequestDto(
                1L,
                "description",
                user,
                LocalDateTime.now(),
                items);
    }

    //!!
    @Test
    void addRequestTest() throws Exception {
        when(requestService.addRequest(any(), anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1L)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.requestor").value(user))
                .andExpect(jsonPath("$.description").value("description"));
    }

    //!!
    @Test
    void getAllYourItemRequestDtoTest() throws Exception {
        //ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        ItemRequestShort itemRequestShort = ItemRequestMapper.toItemRequestShort(itemRequestDto);
        when(requestService.getAllYourItemRequestDto(anyLong())).thenReturn(Collections.singletonList(itemRequestShort));

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].requestor").value(user))
                .andExpect(jsonPath("$[0].description").value("description"));
    }

    @Test
    void getAllOthersItemRequestDtoTest() throws Exception {
        //ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        ItemRequestShort itemRequestShort = ItemRequestMapper.toItemRequestShort(itemRequestDto);
        when(requestService.getAllOthersItemRequestDto(anyLong())).thenReturn(Collections.singletonList(itemRequestShort));

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].requestor").value(user))
                .andExpect(jsonPath("$[0].description").value("description"));
    }

    @Test
    void getItemRequestDtoTest() throws Exception {
        //ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        ItemRequestShort itemRequestShort = ItemRequestMapper.toItemRequestShort(itemRequestDto);
        when(requestService.getItemRequest(anyLong(), anyLong())).thenReturn(itemRequestShort);

        mockMvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.requestor").value(user))
                .andExpect(jsonPath("$.description").value("description"));
    }
}