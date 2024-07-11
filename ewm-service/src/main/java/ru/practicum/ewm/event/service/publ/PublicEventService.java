package ru.practicum.ewm.event.service.publ;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size
    );

    EventFullDto getEvent(Long eventId);

    List<CommentDto> getCommentsByEvent(Long eventId);
}
