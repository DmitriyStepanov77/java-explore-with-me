package ru.practicum.explore.service.compilation;

import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.dto.compilation.UpdateCompilationDto;
import ru.practicum.explore.model.Compilation;

import java.util.List;

public interface CompilationService {
    public Compilation addCompilation(NewCompilationDto compilationDto);

    public void deleteCompilation(int compId);

    public Compilation getCompilation(int compId);

    List<Compilation> getCompilations(Boolean pinned, int from, int size);

    public Compilation updateCompilation(UpdateCompilationDto compilationDto, int compId);
}
