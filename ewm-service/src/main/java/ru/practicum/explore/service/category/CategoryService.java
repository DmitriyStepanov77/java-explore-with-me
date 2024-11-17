package ru.practicum.explore.service.category;

import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.model.Category;

import java.util.List;

public interface CategoryService {

    public Category addCategory(CategoryDto categoryDto);

    public Category getCategory(int categoryId);

    public Category updateCategory(CategoryDto categoryDto, int categoryId);

    public List<Category> getCategories(int from, int size);

    public void deleteCategory(int categoryId);
}
