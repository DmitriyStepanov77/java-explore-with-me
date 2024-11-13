package ru.practicum.explore.controller.compilation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.dto.compilation.UpdateCompilationDto;
import ru.practicum.explore.mapper.CompilationDtoMapper;
import ru.practicum.explore.service.compilation.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;
    private final CompilationDtoMapper compilationDtoMapper;

    @Autowired
    public CompilationAdminController(CompilationService compilationService, CompilationDtoMapper compilationDtoMapper) {

        this.compilationService = compilationService;
        this.compilationDtoMapper = compilationDtoMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        return compilationDtoMapper.toModel(compilationService.addCompilation(compilationDto));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable int compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@Valid @RequestBody UpdateCompilationDto compilationDto,
                                            @PathVariable int compId) {
        return compilationDtoMapper.toModel(compilationService.updateCompilation(compilationDto, compId));
    }
}
