package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Integer> {

}
