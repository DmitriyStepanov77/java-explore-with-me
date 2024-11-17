package ru.practicum.explore.controller.category;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.mapper.CategoryDtoMapper;
import ru.practicum.explore.service.category.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;
    private final CategoryDtoMapper categoryDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryDtoMapper.toModel(categoryService.addCategory(categoryDto));
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                      @PathVariable int catId) {
        return categoryDtoMapper.toModel(categoryService.updateCategory(categoryDto, catId));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int catId) {
        categoryService.deleteCategory(catId);
    }

}
