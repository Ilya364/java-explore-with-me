package ru.practicum.ewm.error.exception;

public class EventCantBeUpdatedException extends RuntimeException {
    public EventCantBeUpdatedException(String message) {
        super(message);
    }
}
