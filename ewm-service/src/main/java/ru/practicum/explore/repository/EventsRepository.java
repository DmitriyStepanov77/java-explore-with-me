package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore.model.Event;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventsRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {

    public List<Event> findByInitiatorId(int initiatorId, Pageable pageable);

    public List<Event> findAllByIdIn(List<Integer> id);

}
