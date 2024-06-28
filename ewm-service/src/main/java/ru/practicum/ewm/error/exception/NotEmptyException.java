package ru.practicum.ewm.error.exception;

public class NotEmptyException extends RuntimeException {
    public NotEmptyException(String message) {
        super(message);
    }
}
