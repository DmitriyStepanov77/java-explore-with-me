package ru.practicum.explore.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.user.UserShortDto;
import ru.practicum.explore.model.enums.EventState;

import java.time.LocalDateTime;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private int id;
    @NotEmpty
    private String annotation;
    @NotNull
    private CategoryDto category;
    @NotNull
    private int confirmedRequests;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @NotEmpty
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private LocationDto location;
    @NotNull
    private boolean paid;
    @NotNull
    private int participantLimit;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    @NotNull
    private boolean requestModeration;
    @NotNull
    private EventState state;
    @NotNull
    private String title;
    @NotNull
    private long views;
}
