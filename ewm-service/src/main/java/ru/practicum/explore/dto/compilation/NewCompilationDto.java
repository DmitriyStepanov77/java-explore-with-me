package ru.practicum.explore.dto.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private List<Integer> events = new ArrayList<>();
    private boolean pinned;
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
