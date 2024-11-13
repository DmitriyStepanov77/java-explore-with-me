package ru.practicum.explore.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.mapper.EventDtoMapper;
import ru.practicum.explore.service.event.EventService;

import ru.practicum.explore.StatClient;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventPublicController {
    private final EventService eventService;
    private final EventDtoMapper eventDtoMapper;
    private final StatClient statClient;

    @Autowired
    public EventPublicController(EventService eventService, EventDtoMapper eventDtoMapper, StatClient statClient) {
        this.eventService = eventService;
        this.eventDtoMapper = eventDtoMapper;
        this.statClient = statClient;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable int eventId, HttpServletRequest request) {
        statClient.saveHit("ewm-service", request);
        return eventDtoMapper.toModel(eventService.getEvent(eventId, true));
    }

    @GetMapping
    public List<EventFullDto> getEventsWithFilter(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Integer> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) String rangeStart,
                                                  @RequestParam(required = false) String rangeEnd,
                                                  @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        statClient.saveHit("ewm-service", request);
        return eventService.getEventsWithFilter(text, categories, paid, rangeStart,
                        rangeEnd, onlyAvailable, sort, from, size).stream()
                .map(eventDtoMapper::toModel).toList();
    }

}
