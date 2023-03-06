package ru.practicum.shareit.booking.repository;

import org.springframework.context.annotation.Lazy;

public class BookingRepositoryImpl {
    private final BookingRepository bookingRepository;

    public BookingRepositoryImpl(@Lazy BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
}
