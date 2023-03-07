package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

//@AutoConfigureMockMvc
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private User user1;
    private User user2;
    private UserDto userDto1;

    private UserDto userDto2;
    private Item item1;

    private ItemDto itemDto1;

    private ItemBooking itemBooking;
    private Map<String, String> map;
    CommentDto commentDto1;

    @BeforeEach
    void beforeEach() {
        LocalDateTime now = LocalDateTime.now();

        user1 = new User(1L, "user1@mail.com", "User1");
        userDto1 = UserMapper.toUserDto(user1);

        user2 = new User(2L, "user2@mail.com", "User2");
        userDto2 = UserMapper.toUserDto(user2);

        map = new HashMap<>();
        map.put("email","user1update@mail.com");

        item1 = new Item(1, "Item1", "Item1_description", true, user1, null);
        itemDto1 = ItemMapper.toItemDto(item1);
        itemBooking = ItemMapper.toItemBooking(item1);

        Comment comment1 = new Comment(1L, "Comment_text", item1, user2);
        commentDto1 = CommentMapper.toCommentDto(comment1);
    }

    @Test
    void addItemTest() throws Exception {
        when(itemService.addItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto1);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ShareItServer.HEADER, userDto1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
    }

    @Test
    void updateTest() throws Exception {
        when((itemService.updateItem(anyLong(), anyMap(), anyLong())))
                .thenReturn(itemDto1);

        mockMvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(map))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ShareItServer.HEADER, userDto1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
    }

    @Test
    void getItemTest() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemBooking);

        mockMvc.perform(get("/items/1")
                        .header(ShareItServer.HEADER, userDto1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemBooking)));
    }

    @Test
    void getAllItemTest() throws Exception {
        when(itemService.getAllItemsUser(anyLong())).thenReturn(List.of(itemBooking));

        mockMvc.perform(get("/items")
                        .header(ShareItServer.HEADER, userDto1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemBooking))));
    }

    @Test
    void searchItemTest() throws Exception {
        when(itemService.searchItem(anyLong(), anyString()))
                .thenReturn(List.of(itemDto1));

        mockMvc.perform(get("/items/search")
                        .param("text", "Item1")
                        .header(ShareItServer.HEADER, userDto1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto1))));
    }

   @Test
    void addComment() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(commentDto1);

        mockMvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ShareItServer.HEADER, userDto1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto1)));
    }
}
