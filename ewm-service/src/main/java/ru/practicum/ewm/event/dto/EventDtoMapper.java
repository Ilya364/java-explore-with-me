package ru.practicum.ewm.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.dto.admin.AdminStateAction;
import ru.practicum.ewm.event.dto.admin.UpdateEventAdminRequest;
import ru.practicum.ewm.event.dto.priv.NewEventDto;
import ru.practicum.ewm.event.dto.priv.UpdateEventUserRequest;
import ru.practicum.ewm.event.dto.priv.UserStateAction;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.category.dto.CategoryDtoMapper.*;
import static ru.practicum.ewm.user.dto.UserDtoMapper.*;

@UtilityClass
public class EventDtoMapper {
    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .paid(event.getPaid())
                .views(event.getViews())
                .eventDate(event.getEventDate())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(toUserShortDto(event.getInitiator()))
                .category(toCategoryDto(event.getCategory()))
                .build();
    }

    public List<EventShortDto> toEventShortDtoList(List<Event> events) {
        return events.stream()
                .map(EventDtoMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .initiator(toUserShortDto(event.getInitiator()))
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .publishedOn(event.getPublishedOn())
                .views(event.getViews())
                .state(event.getState())
                .participantLimit(event.getParticipantsLimit())
                .location(event.getLocation())
                .createdOn(event.getCreatedOn())
                .requestModeration(event.getRequestModeration())
                .description(event.getDescription())
                .category(toCategoryDto(event.getCategory()))
                .build();
    }

    public List<EventFullDto> toEventFullDtoList(List<Event> events) {
        return events.stream()
                .map(EventDtoMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public Event toEvent(NewEventDto dto) {
        return Event.builder()
                .description(dto.getDescription())
                .title(dto.getTitle())
                .createdOn(LocalDateTime.now())
                .location(dto.getLocation())
                .annotation(dto.getAnnotation())
                .participantsLimit(dto.getParticipantLimit())
                .confirmedRequests(0)
                .views(0L)
                .eventDate(dto.getEventDate())
                .requestModeration(dto.getRequestModeration())
                .state(EventState.PENDING)
                .paid(dto.getPaid())
                .build();
    }

    public void partialMapToEvent(Event event, UpdateEventUserRequest dto) {
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLocation(dto.getLocation());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantsLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getStateAction() != null) {
            if (dto.getStateAction().equals(UserStateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            } else if (dto.getStateAction().equals(UserStateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            }
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
    }

    public void partialMapToEvent(Event event, UpdateEventAdminRequest dto) {
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLocation(dto.getLocation());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantsLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getStateAction() != null) {
            if (dto.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (dto.getStateAction().equals(AdminStateAction.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
    }
}
