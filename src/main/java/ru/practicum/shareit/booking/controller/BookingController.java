package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-bookings.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingDtoShort bookingDtoShort,
                                      @RequestHeader Map<String, String> headers){
        String stringIdUserOwner = headers.get("x-sharer-user-id");
        long idUserOwner = Long.parseLong(stringIdUserOwner);
        return bookingService.addBooking(bookingDtoShort, idUserOwner);
    }

    @PatchMapping("{bookingId}")
    public BookingDto updateApproved(@PathVariable("bookingId") Long bookingId,
                                    @RequestHeader Map<String, String> headers,
                                    @RequestParam String approved) {
        String stringIdUserOwner = headers.get("x-sharer-user-id");
        long idUserOwner = Long.parseLong(stringIdUserOwner);
        boolean approvedBoolean = Boolean.parseBoolean(approved);
        return bookingService.updateApproved(bookingId, idUserOwner,approvedBoolean);
    }

    @GetMapping("{id}")
    public BookingDto getBooking(@PathVariable("id") Long id, @RequestHeader Map<String, String> headers) {
        String stringIdUserOwner = headers.get("x-sharer-user-id");
        long idUserOwner = Long.parseLong(stringIdUserOwner);
        return bookingService.getBooking(id, idUserOwner);
    }

    @GetMapping
    public List<BookingDto> getAllUser() {
        return bookingService.getAllBooking();
    }
}
