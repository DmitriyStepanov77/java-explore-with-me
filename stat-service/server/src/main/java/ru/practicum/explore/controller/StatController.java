package ru.practicum.explore.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.HitDto;
import ru.practicum.explore.StatDto;
import ru.practicum.explore.service.StatService;

import java.util.List;

@RestController
public class StatController {
    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto saveHit(@Valid @RequestBody HitDto hitDto) {
        return statService.saveHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getStats(@RequestParam String start,
                                  @RequestParam String end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return statService.getHits(start, end, uris, unique);
    }
}
