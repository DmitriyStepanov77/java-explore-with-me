package ru.practicum.explore.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.explore.model.enums.EventState;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    private Float locationLat;

    private Float locationLon;

    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private String title;

    private EventState state;

    private Long views;
}
