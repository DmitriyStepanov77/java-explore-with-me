package ru.practicum.explore.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    private int id;

    @NotEmpty
    private String name;
}
