package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentDtoMapper;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.error.exception.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto dto) {
        User author = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User %d not found.", userId))
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event %d not found.", eventId))
        );

        Comment comment = CommentDtoMapper.toComment(dto);
        comment.setAuthor(author);
        comment.setEvent(event);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ImpossibleActionForOwnEventException(
              String.format("User %d can't create comment for own event %d.", userId, eventId)
            );
        }
        try {
            CommentDto result = CommentDtoMapper.toCommentDto(commentRepository.save(comment));
            log.info("Comment for event {} by user {} created.", eventId, userId);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException(
                    String.format("Comment for event %d by user %d already created.", eventId, userId)
            );
        }
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto dto) {
        Comment comment = getComment(commentId);
        canCommentBeChanged(userId, comment);
        comment.setText(dto.getText());
        log.info("Comment {} updated.", commentId);
        return CommentDtoMapper.toCommentDto(comment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = getComment(commentId);
        canCommentBeChanged(userId, comment);
        commentRepository.deleteById(commentId);
        log.info("Comment {} deleted.", commentId);
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(String.format("Comment %d not found.", commentId))
        );
    }

    private void canCommentBeChanged(Long userId, Comment comment) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NotOwnerException(String.format("User %d not author of comment %d.", userId, comment.getId()));
        }
        if (comment.getCreated().isBefore(LocalDateTime.now().minusDays(1))) {
            throw new CommentCantBeUpdatedException(
                    String.format("Comment %d can't be updated.", comment.getId())
            );
        }
    }
}
