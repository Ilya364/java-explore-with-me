package ru.practicum.ewm.event.service.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.error.exception.BadRequestException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.publ.search.PublicEventSearchCriteria;
import ru.practicum.ewm.event.service.publ.search.PublicEventSpecification;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.dto.EventDtoMapper.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                         Integer size) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("Start of range must be before end.");
            }
        }
        List<Event> events;
        PublicEventSpecification specification = new PublicEventSpecification(
                PublicEventSearchCriteria.builder()
                        .text(text)
                        .categories(categories)
                        .paid(paid)
                        .rangeStart(rangeStart)
                        .rangeEnd(rangeEnd)
                        .onlyAvailable(onlyAvailable)
                        .sort(sort)
                        .build()
        );
        events = repository.findAll(specification, PageRequest.of(from / size, size)).toList();
        log.info("Events by user received.");
        return toEventShortDtoList(events);
    }

    private List<Event> limitSelection(List<Event> events, Integer from, Integer size) {
        return events.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEvent(Long eventId) {
        Event event = repository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event %d not found.", eventId))
        );
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException(String.format("Event %d not found.", eventId));
        }
        event.setViews(event.getViews() + 1);
        log.info("Event {} received.", eventId);
        return toEventFullDto(event);
    }
}
