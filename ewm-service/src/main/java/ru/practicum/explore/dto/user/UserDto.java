package ru.practicum.explore.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    @NotNull
    @Email
    @Size(min = 6, max = 254)
    private String email;
    @NotEmpty
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
