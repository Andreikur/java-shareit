package ru.practicum.shareit.exception;

public class BookingNotCreatedByUserException extends RuntimeException {
    public BookingNotCreatedByUserException(String message) {
        super(message);
    }
}
