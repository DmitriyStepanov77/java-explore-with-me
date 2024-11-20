package ru.practicum.explore.service.comment;

import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.comment.NewCommentDto;
import ru.practicum.explore.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    public CommentDto addComment(NewCommentDto commentDto, int userId, int eventId);

    public CommentDto getComment(int commentId, int userId);

    public List<CommentDto> getComments(int userId, boolean publicState, int from, int size);

    public CommentDto updateCommentByUser(UpdateCommentDto commentDto, int commentId, int userId);

    public CommentDto updateStatusByAdmin(int commentId, String state);
}
