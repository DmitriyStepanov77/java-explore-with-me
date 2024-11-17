package ru.practicum.explore.service.request;

import ru.practicum.explore.model.Request;

import java.util.List;

public interface RequestService {
    public Request addRequest(int eventId, int userId);

    public List<Request> getRequestsByUser(int userId);

    public Request updateRequestsByUser(int requestId, int userId);
}
