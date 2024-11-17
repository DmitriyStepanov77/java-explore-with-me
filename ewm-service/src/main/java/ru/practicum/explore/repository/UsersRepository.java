package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore.model.User;

public interface UsersRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User> {

}
