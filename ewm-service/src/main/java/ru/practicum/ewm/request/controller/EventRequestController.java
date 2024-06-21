package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.EventRequestService;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequestMapping("/users/{userId}/requests")
@RestController
@RequiredArgsConstructor
public class EventRequestController {
    private final EventRequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getEventRequests(@Positive @PathVariable Long userId) {
        log.info("Request to receive requests of user {}.", userId);
        return service.getEventRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createEventRequest(
            @Positive @RequestParam Long eventId,
            @Positive @PathVariable Long userId
    ) {
        log.info("Request to create request by user {} to event {}.", userId, eventId);
        return service.createEventRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelEventRequest(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long requestId
    ) {
        log.info("Request to cancel request {} by user {}.", userId, requestId);
        return service.cancelEventRequest(userId, requestId);
    }
}
