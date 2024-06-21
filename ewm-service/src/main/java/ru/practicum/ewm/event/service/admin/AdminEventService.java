package ru.practicum.ewm.event.service.admin;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.admin.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    List<EventFullDto> getEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    );

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request);
}
