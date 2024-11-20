package ru.practicum.explore.service.comment;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.comment.NewCommentDto;
import ru.practicum.explore.dto.comment.UpdateCommentDto;
import ru.practicum.explore.exception.ConflictException;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.mapper.CommentDtoMapper;
import ru.practicum.explore.model.Comment;
import ru.practicum.explore.model.enums.CommentState;
import ru.practicum.explore.repository.CommentsRepository;
import ru.practicum.explore.service.event.EventService;
import ru.practicum.explore.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class CommentServiceImp implements CommentService {
    private final CommentDtoMapper commentDtoMapper;
    private final CommentsRepository commentsRepository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public CommentDto addComment(NewCommentDto commentDto, int userId, int eventId) {
        log.info("Added comment to event id = {}, author id = {}, title = {}.",
                eventId, userId, commentDto.getTitle());

        Comment comment = commentDtoMapper.toEntity(commentDto);
        comment.setAuthor(userService.getUser(userId));
        comment.setEvent(eventService.getEvent(eventId, false));
        comment.setCreated(LocalDateTime.now());
        comment.setState(CommentState.PENDING);

        return commentDtoMapper.toModel(commentsRepository.save(comment));
    }

    @Override
    public CommentDto getComment(int commentId, int userId) {
        log.info("Get comment to by id = {}.", commentId);

        Comment comment = getCommentByAuthor(commentId, userId);

        return commentDtoMapper.toModel(comment);
    }

    @Override
    public List<CommentDto> getComments(int userId, boolean publicState, int from, int size) {
        log.info("Get comments.");

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "created"));
        List<Comment> comments;

        if (publicState)
            comments = commentsRepository.findAllByState(CommentState.PUBLISHED, pageable);
        else
            comments = commentsRepository.findAllByAuthorId(userId, pageable);

        return comments.stream()
                .map(commentDtoMapper::toModel).toList();
    }

    @Override
    public CommentDto updateCommentByUser(UpdateCommentDto commentDto, int commentId, int userId) {
        log.info("Update comment id = {}, author id = {}.", commentId, userId);

        Comment comment = getCommentByAuthor(commentId, userId);

        if (comment.getState() != CommentState.PENDING)
            throw new ConflictException("Comment state is not pending");

        Optional.ofNullable(commentDto.getText()).ifPresent(comment::setText);
        Optional.ofNullable(commentDto.getTitle()).ifPresent(comment::setTitle);

        return commentDtoMapper.toModel(comment);
    }

    @Override
    public CommentDto updateStatusByAdmin(int commentId, String state) {
        log.info("Update status by comment id = {}, new status = {}.", commentId, state);

        Comment comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found."));

        if (comment.getState() != CommentState.PENDING)
            throw new ConflictException("Comment state is not pending");

        if (CommentState.convert(state) == CommentState.PUBLISHED)
            comment.setState(CommentState.PUBLISHED);
        else if (CommentState.convert(state) == CommentState.REJECTED)
            comment.setState(CommentState.REJECTED);

        return commentDtoMapper.toModel(comment);
    }

    private Comment getCommentByAuthor(int commentId, int userId) {
        Comment comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found."));

        if (userId != comment.getAuthor().getId()) {
            log.warn("User with id = {} is not author comment. Author id = {}.", userId, comment.getAuthor().getId());
            throw new ConflictException("User is not author comment.");
        }
        return comment;
    }

}
