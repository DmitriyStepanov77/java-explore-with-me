package ru.practicum.explore.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean pinned;

    private String title;

    @ManyToMany
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilations_id"),
            inverseJoinColumns = @JoinColumn(name = "events_id"))
    private List<Event> events = new ArrayList<>();
}
