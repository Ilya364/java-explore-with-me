package ru.practicum.ewm.error.exception;

public class EventRequestNonWaitingStateException extends RuntimeException {
    public EventRequestNonWaitingStateException(String message) {
        super(message);
    }
}
