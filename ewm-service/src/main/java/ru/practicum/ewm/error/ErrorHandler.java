package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.category.controller.admin.AdminCategoryController;
import ru.practicum.ewm.category.controller.publ.PublicCategoryController;
import ru.practicum.ewm.compilation.controller.admin.AdminCompilationController;
import ru.practicum.ewm.compilation.controller.publ.PublicCompilationController;
import ru.practicum.ewm.error.exception.*;
import ru.practicum.ewm.event.controller.admin.AdminEventController;
import ru.practicum.ewm.event.controller.priv.PrivateEventController;
import ru.practicum.ewm.event.controller.publ.PublicEventController;
import ru.practicum.ewm.request.controller.EventRequestController;
import ru.practicum.ewm.user.controller.UserController;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        AdminCategoryController.class, PublicCategoryController.class, AdminCompilationController.class,
        PublicCompilationController.class, AdminEventController.class, PrivateEventController.class,
        PublicEventController.class, EventRequestController.class, UserController.class
})
public class ErrorHandler {
    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestExceptions(final RuntimeException e) {
        log.info("Incorrectly made request: {}", e.getMessage());
        return ApiError.builder()
                .message("Incorrectly made request.")
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParamException(final MissingServletRequestParameterException e) {
        log.info("Incorrectly made request: {}", e.getMessage());
        return ApiError.builder()
                .message("Incorrectly made request.")
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("Not found: {}", e.getMessage());
        return ApiError.builder()
                .message("Not found.")
                .reason(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({NotEmptyException.class, EventDateException.class, EventRequestNonWaitingStateException.class,
            RequestForOwnEventException.class, RequestLimitReachedException.class, EventCantBeUpdatedException.class,
            DuplicateException.class, NotOwnerException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictExceptions(final RuntimeException e) {
        log.info("For the requested operation the conditions are not met: {}", e.getMessage());
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.info("Internal server error: {}", e.getMessage());
        return ApiError.builder()
                .message("Internal server error.")
                .reason(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
