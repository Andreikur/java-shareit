package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDtoShort bookingDtoShort, Long idUserBooker);

    BookingDto updateApproved(long bookingId, long idUserOwner, boolean approved);

    BookingDto getBooking(long id, long idUser);

    List<BookingDto> getAllBooking(long idUserOwner, String state);

    List<BookingDto> getAllBookingOwner(Long idUserOwner, String state);
}
