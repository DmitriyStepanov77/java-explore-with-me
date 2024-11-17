package ru.practicum.explore.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.enums.UserUpdateEventState;

import java.time.LocalDateTime;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserEventDto {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000)
    private String description;
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private UserUpdateEventState stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
