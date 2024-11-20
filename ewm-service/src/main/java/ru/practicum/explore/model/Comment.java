package ru.practicum.explore.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.explore.model.enums.CommentState;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String text;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private CommentState state;
}
