package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore.HitDto;
import ru.practicum.explore.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    public Hit toEntity(HitDto hitDto);

    public HitDto toModel(Hit hit);
}
