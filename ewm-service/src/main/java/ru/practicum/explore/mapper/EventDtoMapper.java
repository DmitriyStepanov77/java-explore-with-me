package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.model.Event;

@Mapper(componentModel = "spring")
public interface EventDtoMapper {
    @Mapping(source = "locationLon", target = "location.lon")
    @Mapping(source = "locationLat", target = "location.lat")
    public EventFullDto toModel(Event event);
}
