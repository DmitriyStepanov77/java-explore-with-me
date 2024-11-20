package ru.practicum.explore.controller.comment;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.comment.UpdateCommentDto;
import ru.practicum.explore.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@AllArgsConstructor
public class CommentPrivateController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@Valid @RequestBody UpdateCommentDto commentDto,
                                    @PathVariable int userId,
                                    @PathVariable int commentId) {
        return commentService.updateCommentByUser(commentDto, commentId, userId);
    }

    @GetMapping
    public List<CommentDto> getComments(@PathVariable int userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return commentService.getComments(userId, false, from, size);
    }

}
