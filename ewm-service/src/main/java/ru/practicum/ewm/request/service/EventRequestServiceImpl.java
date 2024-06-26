package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.error.exception.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.EventRequest;
import ru.practicum.ewm.request.model.EventRequestStatus;
import ru.practicum.ewm.request.repository.EventRequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.request.dto.EventRequestDtoMapper.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventRequestServiceImpl implements EventRequestService {
    private final EventRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId) {
        List<EventRequest> requests = requestRepository.findAllByRequesterId(userId);
        log.info("Requests of user {} received.", userId);
        return toParticipationRequestDtoList(requests);
    }

    @Override
    public ParticipationRequestDto createEventRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id %d not found.", eventId))
        );
        isRequestPossible(userId, event);
        try {
            EventRequestStatus status = EventRequestStatus.PENDING;
            if (event.getParticipantsLimit() == 0 || !event.getRequestModeration()) {
                status = EventRequestStatus.CONFIRMED;
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            }
            EventRequest eventRequest = EventRequest.builder()
                    .status(status)
                    .event(event)
                    .requester(getUserById(userId))
                    .created(LocalDateTime.now())
                    .build();
            ParticipationRequestDto result = toParticipationRequestDto(requestRepository.save(eventRequest));
            log.info("Request by user {} for event {} created.", userId, eventId);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException(
                    String.format("Request from user %d for event %d already exists.", userId, event.getId())
            );
        }
    }

    private void isRequestPossible(Long userId, Event event) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new RequestForOwnEventException("You cannot add a request to your own event");
        }
        if (event.getConfirmedRequests().equals(event.getParticipantsLimit()) && event.getParticipantsLimit() != 0) {
            throw new RequestLimitReachedException("Requests limit is reached.");
        }
        if (event.getState().equals(EventState.PENDING)) {
            throw new EventCantBeUpdatedException("Event must be published.");
        }
    }

    @Transactional(readOnly = true)
    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User %d not found.", userId))
        );
    }

    @Override
    public ParticipationRequestDto cancelEventRequest(Long userId, Long requestId) {
        EventRequest eventRequest = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException(String.format("Request with id %d not found.", requestId))
        );
        if (!eventRequest.getRequester().getId().equals(userId)) {
            throw new NotOwnerException(String.format("User %d not a requester of request %d.", userId, requestId));
        }
        eventRequest.setStatus(EventRequestStatus.CANCELED);
        log.info("Request {} rejected by user {}.", requestId, userId);
        return toParticipationRequestDto(eventRequest);
    }
}
