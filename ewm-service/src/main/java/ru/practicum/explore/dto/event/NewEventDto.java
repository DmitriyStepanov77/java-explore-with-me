package ru.practicum.explore.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Integer category;
    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid = false;
    @PositiveOrZero
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotEmpty
    @Size(min = 3, max = 120)
    private String title;
}
