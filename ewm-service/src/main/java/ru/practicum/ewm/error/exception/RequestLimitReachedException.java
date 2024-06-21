package ru.practicum.ewm.error.exception;

public class RequestLimitReachedException extends RuntimeException {
    public RequestLimitReachedException(String message) {
        super(message);
    }
}
