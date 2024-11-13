package ru.practicum.explore.service.event;

import ru.practicum.explore.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explore.dto.event.NewEventDto;
import ru.practicum.explore.dto.event.UpdateAdminEventDto;
import ru.practicum.explore.dto.event.UpdateUserEventDto;
import ru.practicum.explore.dto.request.RequestUpdateDto;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Request;

import java.util.List;

public interface EventService {

    public Event addEvent(NewEventDto newEventDto, int userId);

    public Event updateUserEvent(UpdateUserEventDto eventDto, int eventId, int userId);

    public Event updateAdminEvent(UpdateAdminEventDto eventDto, int eventId);

    public Event getEvent(int eventId, boolean publicState);

    public List<Event> getEventsByUser(int initiatorId, int from, int size);

    public List<Event> getEventsWithFilter(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                           String rangeEnd, boolean onlyAvailable, String sort, int from, int size);

    public List<Event> getEventsSearch(List<Integer> users, List<String> states, List<Integer> categories,
                                       String rangeStart, String rangeEnd, int from, int size);

    public List<Request> getRequestsByEvent(int userId, int eventId);

    public EventRequestStatusUpdateResult updateRequestStatus(RequestUpdateDto updateDto, int userId, int eventId);
}
