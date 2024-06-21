package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        AdminCategoryController.class, PublicCategoryController.class, AdminCompilationController.class,
        PublicCompilationController.class, AdminEventController.class, PrivateEventController.class,
        PublicEventController.class, EventRequestController.class, UserController.class
})
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        log.info("Bad request: {}", e.getMessage());
        return ApiError.builder()
                .message("Bad request.")
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException e) {
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
    public ApiError handleValidationException(final MethodArgumentNotValidException e) {
        log.info("Incorrectly made request: {}", e.getMessage());
        return ApiError.builder()
                .message("Incorrectly made request.")
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("Object not found: {}", e.getMessage());
        return ApiError.builder()
                .message("Object not found.")
                .reason(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryNotEmptyException(final NotEmptyException e) {
        log.info("For the requested operation the conditions are not met: {}", e.getMessage());
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventDateException(final EventDateException e) {
        log.info("For the requested operation the conditions are not met: {}", e.getMessage());
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventRequestNonWaitingStateException(final EventRequestNonWaitingStateException e) {
        log.info("For the requested operation the conditions are not met: {}", e.getMessage());
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestForOwnEventException(final RequestForOwnEventException e) {
        log.info("For the requested operation the conditions are not met: {}", e.getMessage());
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestLimitReachedException(final RequestLimitReachedException e) {
        log.info("For the requested operation the conditions are not met: {}", e.getMessage());
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventCantBeUpdated(final EventCantBeUpdatedException e) {
        log.info("For the requested operation the conditions are not met: {}", e.getMessage());
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicateRequestException(final DuplicateException e) {
        log.info("For the requested operation the conditions are not met: {}", e.getMessage());
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleDuplicateRequestException(final NotOwnerException e) {
        log.info("For the requested operation the conditions are not met: {}", e.getMessage());
        return ApiError.builder()
                .message("For the requested operation the conditions are not met.")
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
