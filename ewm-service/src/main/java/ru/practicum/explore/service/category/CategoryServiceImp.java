package ru.practicum.explore.service.category;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.mapper.CategoryDtoMapper;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.repository.CategoriesRepository;

import java.util.List;

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoriesRepository categoriesRepository;

    private final CategoryDtoMapper categoryDtoMapper;

    @Override
    public Category addCategory(CategoryDto categoryDto) {
        log.info("Added category by name = {}.", categoryDto.getName());
        return categoriesRepository.save(categoryDtoMapper.toEntity(categoryDto));
    }

    @Override
    public Category getCategory(int categoryId) {
        log.info("Get category by id = {}.", categoryId);
        return categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Error: category is not found."));
    }

    @Override
    public Category updateCategory(CategoryDto categoryDto, int categoryId) {
        log.info("Update category by id = {}.", categoryId);
        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Error: category is not found."));
        category.setName(categoryDto.getName());
        return categoriesRepository.save(category);
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        log.info("Get all categories.");
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        return categoriesRepository.findAll(pageable).getContent();
    }

    @Override
    public void deleteCategory(int categoryId) {
        log.info("Delete category by id = {}.", categoryId);
        categoriesRepository.deleteById(categoryId);
    }
}
