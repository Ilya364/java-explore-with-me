package ru.practicum.ewm.event.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.error.exception.EventCantBeUpdatedException;
import ru.practicum.ewm.error.exception.EventDateException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.admin.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.admin.search.AdminEventSearchCriteria;
import ru.practicum.ewm.event.service.admin.search.AdminEventSpecification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.dto.EventDtoMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository repository;

    @Override
    public List<EventFullDto> getEvents(
            List<Long> users, List<String> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size
    ) {
        AdminEventSpecification eventSpecification = new AdminEventSpecification(
                AdminEventSearchCriteria.builder()
                        .users(users)
                        .states(states)
                        .categories(categories)
                        .rangeStart(rangeStart)
                        .rangeEnd(rangeEnd)
                        .build()
        );
        List<EventFullDto> result = limitSelection(
                toEventFullDtoList(repository.findAll(eventSpecification)), from, size
        );
        log.info("Events by admin received.");
        return result;
    }

    private List<EventFullDto> limitSelection(List<EventFullDto> dtos, Integer from, Integer size) {
        return dtos.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request) {
        Event event = repository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id %s not found.", eventId))
        );
        validateUpdating(event);
        partialMapToEvent(event, request);
        EventFullDto result = toEventFullDto(repository.save(event));
        log.info("Event {} updated.", eventId);
        return result;
    }

    private void validateUpdating(Event event) {
        if (!event.getState().equals(EventState.PENDING)) {
            throw new EventCantBeUpdatedException(
                    String.format(
                            "Cannot update the event because it's not in the right state: %s.", event.getState()
                    )
            );
        }
        if (!event.getEventDate().isAfter((LocalDateTime.now().plusHours(1)))) {
            throw new EventDateException("Event date is an hour.");
        }
    }
}
