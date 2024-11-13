package ru.practicum.explore.dto.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private int id;
    @NotEmpty
    private String annotation;
    @NotEmpty
    private CategoryDto category;
    @NotEmpty
    private Integer confirmedRequests;
    @NotEmpty
    private LocalDateTime eventDate;
    @NotEmpty
    private UserShortDto initiator;
    @NotEmpty
    private Boolean paid;
    @NotEmpty
    private String title;
    @NotEmpty
    private Long views;
}
