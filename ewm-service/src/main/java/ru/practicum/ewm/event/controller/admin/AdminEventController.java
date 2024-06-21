package ru.practicum.ewm.event.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.admin.UpdateEventAdminRequest;
import ru.practicum.ewm.event.service.admin.AdminEventService;
import ru.practicum.ewm.utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequestMapping("/admin/events")
@RestController
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService service;

    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) @RequestParam(required = false) LocalDateTime rangeStart,
            @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) @RequestParam(required = false) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Request to receive events by admin.");
        return service.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest request
    ) {
        log.info("Request to update event by admin.");
        System.out.println(request);
        return service.updateEvent(eventId, request);
    }
}
