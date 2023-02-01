package ru.practicum.shareit.exception;

public class ItemNotCreatedByUserException extends RuntimeException {
    public ItemNotCreatedByUserException(String message) {
        super(message);
    }
}
