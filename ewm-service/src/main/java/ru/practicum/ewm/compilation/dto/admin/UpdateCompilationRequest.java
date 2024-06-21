package ru.practicum.ewm.compilation.dto.admin;

import lombok.Getter;
import ru.practicum.ewm.validation.NullOrNotBlank;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @NullOrNotBlank
    @Size(min = 1, max = 50)
    private String title;
}
