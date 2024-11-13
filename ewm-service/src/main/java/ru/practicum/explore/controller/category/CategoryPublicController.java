package ru.practicum.explore.controller.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.mapper.CategoryDtoMapper;
import ru.practicum.explore.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryPublicController {
    private final CategoryService categoryService;
    private final CategoryDtoMapper categoryDtoMapper;

    @Autowired
    public CategoryPublicController(CategoryService categoryService, CategoryDtoMapper categoryDtoMapper) {
        this.categoryService = categoryService;
        this.categoryDtoMapper = categoryDtoMapper;
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable int catId) {
        return categoryDtoMapper.toModel(categoryService.getCategory(catId));
    }

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return categoryService.getCategories(from, size).stream()
                .map(categoryDtoMapper::toModel).toList();
    }

}
