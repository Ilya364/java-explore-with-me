package ru.practicum.ewm.event.controller.publ;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.client.StatsClient;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.publ.PublicEventService;
import ru.practicum.ewm.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final PublicEventService service;
    private final StatsClient statsClient = new StatsClient(new RestTemplate());

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) @RequestParam(required = false)
            LocalDateTime rangeStart,
            @DateTimeFormat(pattern = Constants.DATE_TIME_PATTERN) @RequestParam(required = false)
            LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) throws JsonProcessingException {
        log.info("Request to get events by user.");
        statsClient.postHit("explore-with-me", "/events", request.getRemoteAddr(), LocalDateTime.now());
        return service.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(
            @Positive @PathVariable Long eventId,
            HttpServletRequest request
    ) throws JsonProcessingException {
        log.info("Request to get event {} by user.", eventId);
        statsClient.postHit("explore-with-me", "/events/" + eventId, request.getRemoteAddr(), LocalDateTime.now());
        return service.getEvent(eventId);
    }
}
