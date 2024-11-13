package ru.practicum.explore.controller.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.mapper.CompilationDtoMapper;
import ru.practicum.explore.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
public class CompilationPublicController {
    private final CompilationService compilationService;
    private final CompilationDtoMapper compilationDtoMapper;

    @Autowired
    public CompilationPublicController(CompilationService compilationService, CompilationDtoMapper compilationDtoMapper) {

        this.compilationService = compilationService;
        this.compilationDtoMapper = compilationDtoMapper;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilations(pinned, from, size).stream()
                .map(compilationDtoMapper::toModel).toList();
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable int compId) {
        return compilationDtoMapper.toModel(compilationService.getCompilation(compId));
    }

}