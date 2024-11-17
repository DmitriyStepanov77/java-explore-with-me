package ru.practicum.explore.dto.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private int id;
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
