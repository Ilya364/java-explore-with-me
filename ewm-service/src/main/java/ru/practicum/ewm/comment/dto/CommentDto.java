package ru.practicum.ewm.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDto {
    private Long id;
    private Long authorId;
    private Long eventId;
    private LocalDateTime created;
    private String text;
}
