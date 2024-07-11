package ru.practicum.ewm.error.exception;

public class CommentCantBeUpdatedException extends RuntimeException {
    public CommentCantBeUpdatedException(String message) {
        super(message);
    }
}
