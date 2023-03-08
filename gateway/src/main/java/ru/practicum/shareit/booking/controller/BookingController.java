package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.exception.BookingAvailableException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Validated
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(ShareItGateway.HEADER) long id,
                                         @Valid @RequestBody BookingDtoShort bookingDtoShort) {

        if (LocalDateTime.now().isAfter(bookingDtoShort.getStart())) {
            log.info("Дата начала бронирования должна быть в будующем");
            throw new BookingAvailableException(String.format("Дата начала бронирования должна быть в будующем"));
        }

        if (bookingDtoShort.getEnd().compareTo(bookingDtoShort.getStart()) < 0) {
            log.info("Дата начала бронирования позже даты конца");
            throw new BookingAvailableException(String.format("Дата начала бронирования позже даты конца"));
        }
        return bookingClient.addBooking(id, bookingDtoShort);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateApproved(@RequestHeader(ShareItGateway.HEADER) long userId,
                                               @PathVariable long bookingId,
                                               @RequestParam boolean approved) {
        return bookingClient.updateApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(ShareItGateway.HEADER) long userId,
                                          @PathVariable long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBooking(@RequestHeader(ShareItGateway.HEADER) long userId,
                                              @RequestParam(defaultValue = "ALL", required = false) String state,
                                              @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                              @Positive @RequestParam(defaultValue = "20", required = false) int size) {

        try {
            Enum.valueOf(State.class, state);
        } catch (IllegalArgumentException e) {
            throw new BookingAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookingClient.getAllBooking(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingOwner(@RequestHeader(ShareItGateway.HEADER) long userId,
                                             @RequestParam(defaultValue = "ALL", required = false) String state,
                                             @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                             @Positive @RequestParam(defaultValue = "20", required = false) int size) {
        try {
            Enum.valueOf(State.class, state);
        } catch (IllegalArgumentException e) {
            throw new BookingAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookingClient.getAllBookingOwner(userId, state, from, size);
    }
}
