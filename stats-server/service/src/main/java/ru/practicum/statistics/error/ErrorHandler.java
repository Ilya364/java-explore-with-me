package ru.practicum.statistics.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.statistics.controller.StatsController;

import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = StatsController.class)
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> handleDateRangeException(final DateRangeException e) {
        log.info("Error in date range: {}", e.getMessage());
        return Map.of("Error in date range: ", e.getMessage());
    }
}
