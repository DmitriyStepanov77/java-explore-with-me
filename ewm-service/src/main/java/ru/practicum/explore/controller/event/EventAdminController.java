package ru.practicum.explore.controller.event;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.UpdateAdminEventDto;
import ru.practicum.explore.mapper.EventDtoMapper;
import ru.practicum.explore.service.event.EventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService eventService;
    private final EventDtoMapper eventDtoMapper;

    @Autowired
    public EventAdminController(EventService eventService, EventDtoMapper eventDtoMapper) {
        this.eventService = eventService;
        this.eventDtoMapper = eventDtoMapper;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@Valid @RequestBody UpdateAdminEventDto eventDto,
                                    @PathVariable int eventId) {
        return eventDtoMapper.toModel(eventService.updateAdminEvent(eventDto, eventId));
    }

    @GetMapping
    public List<EventFullDto> getEventsSearch(@RequestParam(required = false) List<Integer> users,
                                              @RequestParam(required = false) List<String> states,
                                              @RequestParam(required = false) List<Integer> categories,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false) String rangeEnd,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsSearch(users, states, categories, rangeStart, rangeEnd, from, size).stream()
                .map(eventDtoMapper::toModel).toList();
    }

}
