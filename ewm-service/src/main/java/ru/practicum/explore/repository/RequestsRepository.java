package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<Request, Integer> {

    public List<Request> findByRequesterId(int requesterId);

    public Optional<Request> findByIdAndRequesterId(int id, int requesterId);

    public List<Request> findByEventId(int eventId);

    public List<Request> findAllByIdIn(List<Integer> ids);

    public Optional<Request> findByEventIdAndRequesterId(int eventId, int requesterId);

}
