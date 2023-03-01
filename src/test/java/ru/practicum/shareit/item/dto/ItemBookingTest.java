package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.enums.StatusBooking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemBookingTest {

    @Autowired
    private JacksonTester<ItemBooking> json;

    private ItemBooking itemBooking;

    @BeforeEach
    void beforeEach() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        User user1 = new User(1L, "User1 name", "user1@mail.com");
        User user2 = new User(2L, "User2 name", "user2@mail.com");

        ItemRequest itemRequest1 = new ItemRequest(1L, "ItemRequest1 description", user1, now);

        Item item1 = new Item(1, "Item1", "Item_description", true, user1, itemRequest1);

        itemBooking = ItemMapper.toItemBooking(item1);
        Booking booking1 = new Booking(1, start, end, item1, user2, StatusBooking.WAITING);

        BookingDtoShort bookingDtoShort1 = BookingMapper.toBookingDtoShort(booking1);

        Booking booking2 = new  Booking(1, start, end, item1, user2, StatusBooking.WAITING);
        BookingDtoShort bookingDtoShort2 = BookingMapper.toBookingDtoShort(booking2);
        itemBooking.setLastBooking(bookingDtoShort1);
        itemBooking.setNextBooking(bookingDtoShort2);

        Comment comment1 = new Comment(1L, "Comment1 text", item1, user2);

        CommentDto comment1Dto = CommentMapper.toCommentDto(comment1);
        itemBooking.setComments(List.of(comment1Dto));
    }

    @Test
    void testSerialize() throws Exception {
        JsonContent<ItemBooking> result = json.write(itemBooking);

        Integer value = Math.toIntExact(itemBooking.getId());
        Integer lasBookingId = Math.toIntExact(itemBooking.getLastBooking().getId());
        Integer nextBookingId = Math.toIntExact(itemBooking.getNextBooking().getId());

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(value);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemBooking.getName());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemBooking.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemBooking.getAvailable());
        assertThat(result).extractingJsonPathNumberValue(
                "$.lastBooking.id").isEqualTo(lasBookingId);
        assertThat(result).extractingJsonPathNumberValue(
                "$.nextBooking.id").isEqualTo(nextBookingId);
    }
}
