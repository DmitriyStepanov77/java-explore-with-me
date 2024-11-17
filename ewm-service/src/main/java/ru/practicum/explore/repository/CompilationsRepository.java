package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore.model.Compilation;

public interface CompilationsRepository extends JpaRepository<Compilation, Integer>, QuerydslPredicateExecutor<Compilation> {

}
