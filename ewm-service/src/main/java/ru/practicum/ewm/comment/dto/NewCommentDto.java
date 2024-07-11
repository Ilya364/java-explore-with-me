package ru.practicum.ewm.comment.dto;

import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Validated
@Getter
public class NewCommentDto {
    @NotBlank
    @Size(min = 1, max = 280)
    private String text;
}
