package ru.practicum.ewm.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.request.model.EventRequest;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventRequestDtoMapper {
    public ParticipationRequestDto toParticipationRequestDto(EventRequest eventRequest) {
        return ParticipationRequestDto.builder()
                .id(eventRequest.getId())
                .event(eventRequest.getEvent().getId())
                .requester(eventRequest.getRequester().getId())
                .status(eventRequest.getStatus())
                .created(eventRequest.getCreated())
                .build();
    }

    public List<ParticipationRequestDto> toParticipationRequestDtoList(List<EventRequest> requests) {
        return requests.stream()
                .map(EventRequestDtoMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
