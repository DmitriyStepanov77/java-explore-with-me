package ru.practicum.explore.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.enums.CommentState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private int id;

    private String title;

    private String text;

    private String author;

    private String event;

    private LocalDateTime created;

    private CommentState state;
}
