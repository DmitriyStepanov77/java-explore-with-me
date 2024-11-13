package ru.practicum.explore.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.request.RequestDto;
import ru.practicum.explore.mapper.RequestDtoMapper;
import ru.practicum.explore.service.request.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
public class RequestPrivateController {
    private final RequestService requestService;
    private final RequestDtoMapper requestDtoMapper;

    @Autowired
    public RequestPrivateController(RequestService requestService, RequestDtoMapper requestDtoMapper) {

        this.requestService = requestService;
        this.requestDtoMapper = requestDtoMapper;
    }

    @GetMapping
    public List<RequestDto> getRequestsByUser(@PathVariable int userId) {
        return requestService.getRequestsByUser(userId).stream()
                .map(requestDtoMapper::toModel)
                .toList();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable int userId, @RequestParam int eventId) {
        return requestDtoMapper.toModel(requestService.addRequest(eventId, userId));
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto deleteRequest(@PathVariable int requestId,
                                    @PathVariable int userId) {
        return requestDtoMapper.toModel(requestService.updateRequestsByUser(requestId, userId));
    }

}
