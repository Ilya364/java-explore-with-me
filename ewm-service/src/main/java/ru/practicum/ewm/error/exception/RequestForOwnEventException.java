package ru.practicum.ewm.error.exception;

public class RequestForOwnEventException extends RuntimeException {
    public RequestForOwnEventException(String message) {
        super(message);
    }
}
