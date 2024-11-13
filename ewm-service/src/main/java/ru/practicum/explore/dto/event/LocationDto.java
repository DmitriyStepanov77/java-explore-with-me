package ru.practicum.explore.dto.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @NotEmpty
    private Float lat;
    @NotEmpty
    private Float lon;
}
