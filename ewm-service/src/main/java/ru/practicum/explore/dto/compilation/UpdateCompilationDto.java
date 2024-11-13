package ru.practicum.explore.dto.compilation;

import jakarta.validation.Valid;
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
public class UpdateCompilationDto {

    private List<Integer> events = new ArrayList<>();
    private Boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
}
