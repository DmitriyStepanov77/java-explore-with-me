package ru.practicum.explore.dto.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.dto.event.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private int id;
    @NotEmpty
    private List<EventShortDto> events = new ArrayList<>();
    private boolean pinned;
    @NotEmpty
    private String title;
}
