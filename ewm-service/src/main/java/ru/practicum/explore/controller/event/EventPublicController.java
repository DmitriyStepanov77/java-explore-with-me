package ru.practicum.explore.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.mapper.EventDtoMapper;
import ru.practicum.explore.service.comment.CommentService;
import ru.practicum.explore.service.event.EventService;

import ru.practicum.explore.StatClient;

import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventPublicController {
    private final EventService eventService;
    private final EventDtoMapper eventDtoMapper;
    private final StatClient statClient;
    private final CommentService commentService;

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

    @GetMapping("/{eventId}/comments")
    public List<CommentDto> getComments(@PathVariable int eventId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return commentService.getComments(0, true, from, size);
    }

}
