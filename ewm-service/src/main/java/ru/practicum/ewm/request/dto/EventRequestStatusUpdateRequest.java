package ru.practicum.ewm.request.dto;

import lombok.Getter;
import ru.practicum.ewm.request.model.EventRequestStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class EventRequestStatusUpdateRequest {
    @NotEmpty
    private List<Long> requestIds;
    @NotNull
    private EventRequestStatus status;
}
