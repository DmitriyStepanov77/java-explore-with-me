package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationDtoMapper {
    @Mapping(target = "events", ignore = true)
    public Compilation toEntity(NewCompilationDto newCompilationDto);

    public CompilationDto toModel(Compilation compilation);
}
