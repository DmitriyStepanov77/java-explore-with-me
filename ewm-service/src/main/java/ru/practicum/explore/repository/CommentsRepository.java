package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore.model.Comment;
import ru.practicum.explore.model.enums.CommentState;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Integer> {

    public List<Comment> findAllByAuthorId(int authorId, Pageable pageable);

    public List<Comment> findAllByState(CommentState state, Pageable pageable);

}
