package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventRequestService {
    List<ParticipationRequestDto> getEventRequests(Long userId);

    ParticipationRequestDto createEventRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelEventRequest(Long userId, Long requestId);
}
