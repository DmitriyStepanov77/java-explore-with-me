package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {
    public Category toEntity(CategoryDto categoryDto);

    public CategoryDto toModel(Category category);
}
