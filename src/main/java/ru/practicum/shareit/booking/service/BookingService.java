package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDtoShort bookingDtoShort, Long idUserOwner);
    BookingDto updateApproved(Long bookingId, long idUserOwner, boolean approved);
    BookingDto getBooking(long id, long idUserOwner);
    List<BookingDto> getAllBooking();
}
