package ru.practicum.shareit.exception;

public class MailNotUniqueException extends RuntimeException {
    public MailNotUniqueException(String message) {
        super(message);
    }
}
