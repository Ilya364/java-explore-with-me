package ru.practicum.ewm.event.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.utils.Constants;
import ru.practicum.ewm.validation.AfterTwoHours;
import ru.practicum.ewm.validation.NullOrNotBlank;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@ToString
@Getter
public class UpdateEventAdminRequest {
    @NullOrNotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @Positive
    private Long category;
    @NullOrNotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @AfterTwoHours
    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private AdminStateAction stateAction;
    @NullOrNotBlank
    @Size(min = 3, max = 120)
    private String title;
}
