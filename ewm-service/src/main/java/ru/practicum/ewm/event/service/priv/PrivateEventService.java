package ru.practicum.ewm.event.service.priv;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.priv.NewEventDto;
import ru.practicum.ewm.event.dto.priv.UpdateEventUserRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getCurrentUserEvents(Long userId, Long from, Long size);

    EventFullDto createEvent(Long userId, NewEventDto dto);

    EventFullDto getCurrentUserEvent(Long userId, Long eventId);

    EventFullDto updateCurrentUserEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    List<ParticipationRequestDto> getCurrentUserEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequestsStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequest request
    );
}
