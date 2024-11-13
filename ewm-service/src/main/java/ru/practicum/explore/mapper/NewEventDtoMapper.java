package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore.dto.event.NewEventDto;
import ru.practicum.explore.model.Event;

@Mapper(componentModel = "spring")
public interface NewEventDtoMapper {
    @Mapping(source = "category", target = "category.id")
    @Mapping(source = "location.lat", target = "locationLat")
    @Mapping(source = "location.lon", target = "locationLon")
    public Event toEntity(NewEventDto newEventDto);

}
