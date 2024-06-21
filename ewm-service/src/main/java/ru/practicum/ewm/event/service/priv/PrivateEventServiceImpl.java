package ru.practicum.ewm.event.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.exception.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.priv.NewEventDto;
import ru.practicum.ewm.event.dto.priv.UpdateEventUserRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.EventRequest;
import ru.practicum.ewm.request.model.EventRequestStatus;
import ru.practicum.ewm.request.repository.EventRequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.dto.EventDtoMapper.*;
import static ru.practicum.ewm.request.dto.EventRequestDtoMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventRequestRepository eventRequestRepository;
    private final UserRepository userRepository;

    @Override
    public List<EventShortDto> getCurrentUserEvents(Long userId, Long from, Long size) {
        List<Event> events = eventRepository.findAllByInitiator(userId, from, size);
        log.info("User {} events received.", userId);
        return toEventShortDtoList(events);
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto dto) {
        Category category = categoryRepository.findById(dto.getCategory()).orElseThrow(
                () -> new NotFoundException(String.format("Category %d not found.", dto.getCategory()))
        );
        User initiator = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User %d not found.", userId))
        );

        Event event = toEvent(dto);
        event.setCategory(category);
        event.setInitiator(initiator);

        EventFullDto result = toEventFullDto(eventRepository.save(event));
        log.info("Event {} of user {} created.", result.getId(), userId);
        return result;
    }

    @Override
    public EventFullDto getCurrentUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event %d not found.", eventId))
        );
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(String.format("Event %d for initiator %d not found.", eventId, userId));
        }
        log.info("Event {} of user {} received.", eventId, userId);
        return toEventFullDto(event);
    }

    @Override
    public EventFullDto updateCurrentUserEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        Event event = getEvent(eventId);
        isUserInitiator(userId, event);
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new EventCantBeUpdatedException("Only pending or canceled events can be changed.");
        }
        partialMapToEvent(event, request);
        log.info("Event {} updated by user {}.", eventId, userId);
        return toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getCurrentUserEventRequests(Long userId, Long eventId) {
        List<ParticipationRequestDto> requests = toParticipationRequestDtoList(
                eventRequestRepository.findAllByEventId(eventId)
        );
        log.info("Requests of user {} and event {} received.", userId, eventId);
        return requests;
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestsStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequest statusUpdateRequest
    ) {
        Event event = getEvent(eventId);
        if (event.getConfirmedRequests().equals(event.getParticipantsLimit())) {
            throw new RequestLimitReachedException("The participant limit has been reached");
        }
        checkEventAndUser(userId, statusUpdateRequest, event);

        EventRequestStatus newStatus = statusUpdateRequest.getStatus();
        boolean isNewStatusConfirmed = newStatus.equals(EventRequestStatus.CONFIRMED);
        List<EventRequest> requests = eventRequestRepository.findAllById(statusUpdateRequest.getRequestIds());
        if ((!event.getRequestModeration() || event.getParticipantsLimit() == 0) && isNewStatusConfirmed) {
            return confirmAllRequests(statusUpdateRequest, event, requests);
        }

        for (EventRequest request : requests) {
            if (request.getStatus().equals(EventRequestStatus.PENDING)) {
                if (event.getConfirmedRequests() < event.getParticipantsLimit() && isNewStatusConfirmed) {
                    request.setStatus(newStatus);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else {
                    request.setStatus(EventRequestStatus.REJECTED);
                }
            } else {
                throw new EventRequestNonWaitingStateException(
                        String.format("Event request %d already %s.",
                                request.getId(), request.getStatus().toString().toLowerCase()
                        )
                );
            }
        }
        eventRepository.save(event);
        log.info("Requests status for event {} updated.", eventId);
        return buildRequestsStatusUpdateResult(requests);
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id %d not found.", eventId))
        );
    }

    private void checkEventAndUser(Long userId, EventRequestStatusUpdateRequest statusUpdateRequest, Event event) {
        isUserInitiator(userId, event);
        limitIsReached(event, statusUpdateRequest);
    }

    private void isUserInitiator(Long userId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotOwnerException(String.format("User %d not initiator of event %d.", userId, event.getId()));
        }
    }

    private void limitIsReached(Event event, EventRequestStatusUpdateRequest request) {
        if (event.getConfirmedRequests().equals(event.getParticipantsLimit())
                && event.getParticipantsLimit() > 0) {
            throw new RequestLimitReachedException("The participant limit has been reached");
        }
    }

    private EventRequestStatusUpdateResult confirmAllRequests(
            EventRequestStatusUpdateRequest statusUpdateRequest, Event event, List<EventRequest> requests
    ) {
        event.setConfirmedRequests(event.getConfirmedRequests() + statusUpdateRequest.getRequestIds().size());
        requests.forEach(eventRequest -> eventRequest.setStatus(EventRequestStatus.CONFIRMED));
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(toParticipationRequestDtoList(requests))
                .build();
    }

    private EventRequestStatusUpdateResult buildRequestsStatusUpdateResult(List<EventRequest> requests) {
        List<ParticipationRequestDto> confirmed = toParticipationRequestDtoList(
                requests.stream()
                        .filter(r -> r.getStatus().equals(EventRequestStatus.CONFIRMED))
                        .collect(Collectors.toList())
        );
        List<ParticipationRequestDto> rejected = toParticipationRequestDtoList(
                requests.stream()
                        .filter(r -> r.getStatus().equals(EventRequestStatus.REJECTED))
                        .collect(Collectors.toList())
        );
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
    }
}
