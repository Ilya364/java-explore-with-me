package ru.practicum.ewm.event.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.priv.NewEventDto;
import ru.practicum.ewm.event.dto.priv.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.priv.PrivateEventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RequestMapping("/users/{userId}/events")
@RestController
@RequiredArgsConstructor
public class PrivateEventController {
    private final PrivateEventService service;

    @GetMapping
    public List<EventShortDto> getCurrentUserEvents(
            @Positive @PathVariable Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Long from,
            @Positive @RequestParam(defaultValue = "10") Long size
    ) {
        log.info("Request to receive events of user {}.", userId);
        return service.getCurrentUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(
            @Positive @PathVariable Long userId,
            @Valid @RequestBody NewEventDto dto
    ) {
        log.info("Request to create event of user {}.", userId);
        System.out.println(dto);
        return service.createEvent(userId, dto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getCurrentUserEvent(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId
    ) {
        log.info("Request to receive event {} of user {}.", eventId, userId);
        return service.getCurrentUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateCurrentUserEvent(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequest request
    ) {
        log.info("Request to update event {} by user {}.", eventId, userId);
        return service.updateCurrentUserEvent(userId, eventId, request);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getCurrentUserEventRequests(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId
    ) {
        log.info("Request to receive event requests {} of user {}.", eventId, userId);
        return service.getCurrentUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateCurrentUserEventRequestsStatus(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody(required = false) EventRequestStatusUpdateRequest request
    ) {
        log.info("Request to update event requests for event {} by user {}.", eventId, userId);
        return service.updateEventRequestsStatus(userId, eventId, request);
    }
}
