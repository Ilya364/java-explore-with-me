package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.request.model.EventRequestStatus;
import ru.practicum.ewm.utils.Constants;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ParticipationRequestDto {
    @Positive
    private Long id;
    @Positive
    private Long event;
    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime created;
    @Positive
    private Long requester;
    private EventRequestStatus status;
}
