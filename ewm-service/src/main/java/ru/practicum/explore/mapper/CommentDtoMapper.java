package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.comment.NewCommentDto;
import ru.practicum.explore.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentDtoMapper {
    public Comment toEntity(NewCommentDto newCommentDto);

    @Mapping(source = "author.name", target = "author")
    @Mapping(source = "event.title", target = "event")
    public CommentDto toModel(Comment comment);
}
