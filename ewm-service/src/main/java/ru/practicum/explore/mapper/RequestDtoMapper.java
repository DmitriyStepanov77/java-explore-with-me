package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore.dto.request.RequestDto;
import ru.practicum.explore.model.Request;

@Mapper(componentModel = "spring")
public interface RequestDtoMapper {
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    public RequestDto toModel(Request request);
}
