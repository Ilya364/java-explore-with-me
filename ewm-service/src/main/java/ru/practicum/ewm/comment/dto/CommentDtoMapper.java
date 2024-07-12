package ru.practicum.ewm.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentDtoMapper {
    public Comment toComment(NewCommentDto dto) {
        return Comment.builder()
                .text(dto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public List<CommentDto> toCommentDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentDtoMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
