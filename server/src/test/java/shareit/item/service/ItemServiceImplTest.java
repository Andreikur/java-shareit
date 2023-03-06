package shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.enums.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    private User user1;

    private User user2;

    private Item item1;

    private Booking booking1;

    private Comment comment1;

    Map<String, String> itemData;

    @BeforeEach
    void beforeEach() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        user1 = new User(1L, "user1@mail.com", "User1 name");
        userRepository.save(user1);
        user2 = new User(2L, "user2@mail.com", "User2 name");
        userRepository.save(user2);
        item1 = new Item(1L, "Item1 name", "Item1 description", true, user1, null);
        booking1 = new Booking(1L, start, end, item1, user2, StatusBooking.WAITING);
        comment1 = new Comment(1L, "Comment1 text", item1, user2);
        itemData = new HashMap<>() {{
            put("name", "Item1update");
            put("description", "Item_descriptionUpdate");
            put("available", "true");
        }};
    }

    @Test
    void addItemTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(itemRepository.save(any(Item.class)))
                .thenReturn(item1);

        ItemDto itemDto = itemService.addItem(ItemMapper.toItemDto(item1), item1.getId());

        assertEquals(1, itemDto.getId());
        assertEquals("Item1 name", itemDto.getName());
        assertEquals("Item1 description", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertNull(itemDto.getRequestId());
    }

    @Test
    void addItemWhenUserNotFoundExceptionThrownTest() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь с таким id не найден"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> itemService.addItem(ItemMapper.toItemDto(item1), 3));

        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void updateTest() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item1);

        ItemDto itemDto = itemService.updateItem(item1.getId(), itemData, user1.getId());

        assertEquals(1, itemDto.getId());
        assertEquals("Item1update", itemDto.getName());
        assertEquals("Item_descriptionUpdate", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertNull(itemDto.getRequestId());
    }

    @Test
    void updateItemFromNotItemTest() {
        when(itemRepository.findById(anyLong())).thenThrow(new ItemNotFoundException("Вещь с таким id не найдена"));

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> itemService.updateItem(item1.getId(), itemData, user1.getId()));

        assertEquals("Вещь с таким id не найдена", exception.getMessage());
    }

    @Test
    void getItemTest() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));

        ItemBooking itemDtoBooking = itemService.getItem(user1.getId(), item1.getId());

        assertEquals(1, itemDtoBooking.getId());
        assertEquals("Item1 name", itemDtoBooking.getName());
        assertEquals("Item1 description", itemDtoBooking.getDescription());
        assertEquals(true, itemDtoBooking.getAvailable());
    }

    @Test
    void getItemFromNotItemTest() {
        when(itemRepository.findById(anyLong())).thenThrow(new ItemNotFoundException("Вещь с таким id не найдена"));

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> itemService.getItem(user1.getId(), 3));

        assertEquals("Вещь с таким id не найдена", exception.getMessage());
    }

    @Test
    void getAllItemsUserTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong()))
                .thenReturn(List.of(item1));

        List<ItemBooking> itemBookings = itemService.getAllItemsUser(user1.getId());

        assertEquals(1, itemBookings.size());
        assertEquals(1, itemBookings.get(0).getId());
        assertEquals("Item1 name", itemBookings.get(0).getName());
        assertEquals("Item1 description", itemBookings.get(0).getDescription());
    }

    @Test
    void searchItemTest() {

        List<ItemDto> itemDtoList = itemService.searchItem(1, "Item1");

        assertEquals(Collections.emptyList(), itemDtoList);
    }

    @Test
    void searchItemWithBlancText() {

        when(itemRepository.findAll())
                .thenReturn(List.of(item1));
        List<ItemDto> itemDtoList = itemService.searchItem(1, "");

        assertEquals(Collections.emptyList(), itemDtoList);
    }

    @Test
    void addComment() {
        when(bookingRepository.findByBookerIdAndItemIdAndEndBefore(
                anyLong(),
                anyLong(),
                any(LocalDateTime.class)))
                .thenReturn(Optional.ofNullable(booking1));

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment1);

        CommentDto commentDto = itemService
                .addComment(1L, 1L, CommentMapper.toCommentDto(comment1));

        assertEquals(1, commentDto.getId());
        assertEquals("Comment1 text", commentDto.getText());
        assertEquals("User1 name", commentDto.getAuthorName());
    }

    @Test
    void createCommentTest() {
        when(bookingRepository.findByBookerIdAndItemIdAndEndBefore(
                anyLong(),
                anyLong(),
                any(LocalDateTime.class)))
                .thenReturn(Optional.of(booking1));

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment1);

        CommentDto commentDto = itemService.addComment(
                1,
                1,
                CommentMapper.toCommentDto(comment1)
        );

        assertEquals(1, commentDto.getId());
        assertEquals("Comment1 text", commentDto.getText());
        assertEquals("User1 name", commentDto.getAuthorName());
    }

    @Test
    void createCommentFromUserWithoutBookingTest() {
        when(bookingRepository.findByBookerIdAndItemIdAndEndBefore(
                anyLong(),
                anyLong(),
                any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> itemService.addComment(
                        1,
                        1,
                        CommentMapper.toCommentDto(comment1)
                ));

        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }
}

