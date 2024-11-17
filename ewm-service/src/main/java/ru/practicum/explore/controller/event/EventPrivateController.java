package ru.practicum.explore.controller.event;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explore.dto.event.NewEventDto;
import ru.practicum.explore.dto.event.UpdateUserEventDto;
import ru.practicum.explore.dto.request.RequestDto;
import ru.practicum.explore.dto.request.RequestUpdateDto;
import ru.practicum.explore.mapper.EventDtoMapper;
import ru.practicum.explore.mapper.RequestDtoMapper;
import ru.practicum.explore.service.event.EventService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class EventPrivateController {
    private final EventService eventService;
    private final EventDtoMapper eventDtoMapper;
    private final RequestDtoMapper requestDtoMapper;

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable int eventId) {
        return eventDtoMapper.toModel(eventService.getEvent(eventId, false));
    }

    @GetMapping
    public List<EventFullDto> getEventsByUser(@PathVariable int userId,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsByUser(userId, from, size).stream()
                .map(eventDtoMapper::toModel)
                .toList();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@Valid @RequestBody NewEventDto newEventDto,
                                 @PathVariable int userId) {
        return eventDtoMapper.toModel(eventService.addEvent(newEventDto, userId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@Valid @RequestBody UpdateUserEventDto eventDto,
                                    @PathVariable int userId,
                                    @PathVariable int eventId) {
        return eventDtoMapper.toModel(eventService.updateUserEvent(eventDto, eventId, userId));
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestByEvent(@PathVariable int userId,
                                              @PathVariable int eventId) {
        return eventService.getRequestsByEvent(userId, eventId).stream()
                .map(requestDtoMapper::toModel)
                .toList();
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(@RequestBody RequestUpdateDto updateDto,
                                                               @PathVariable int userId,
                                                               @PathVariable int eventId) {
        return eventService.updateRequestStatus(updateDto, userId, eventId);
    }

}
