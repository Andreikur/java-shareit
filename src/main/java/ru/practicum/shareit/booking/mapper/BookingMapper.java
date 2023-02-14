package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    private BookingMapper(){
    }

    public static BookingDto toBookingDto(Booking booking){
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static List<BookingDto> toBookingDto (Iterable<Booking> bookings){
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookings){
            bookingDtoList.add(toBookingDto(booking));
        }
        return bookingDtoList;
    }

    public static Booking toBooking(BookingDto bookingDto){
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItem(),
                bookingDto.getBooker(),
                bookingDto.getStatus()
        );
    }

    public static BookingDto toBookingTdo(BookingDtoShort bookingDtoShort){
        return new BookingDto(
                bookingDtoShort.getId(),
                bookingDtoShort.getStart(),
                bookingDtoShort.getEnd()
        );
    }
}
