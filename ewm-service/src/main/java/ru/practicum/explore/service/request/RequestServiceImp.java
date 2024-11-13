package ru.practicum.explore.service.request;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.exception.ConflictException;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Request;
import ru.practicum.explore.model.User;
import ru.practicum.explore.model.enums.EventState;
import ru.practicum.explore.model.enums.RequestState;
import ru.practicum.explore.repository.EventsRepository;
import ru.practicum.explore.repository.RequestsRepository;
import ru.practicum.explore.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Log4j2
public class RequestServiceImp implements RequestService {

    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public RequestServiceImp(RequestsRepository requestsRepository, EventsRepository eventsRepository, UsersRepository usersRepository) {
        this.requestsRepository = requestsRepository;
        this.eventsRepository = eventsRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public Request addRequest(int eventId, int userId) {
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found."));
        User user = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));

        if (event.getInitiator().equals(user) || !event.getState().equals(EventState.PUBLISHED)
                || requestsRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()
                || (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= (event.getParticipantLimit())))
            throw new ConflictException("Request is incorrect");

        Request request = Request.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now().withNano(0))
                .status(event.getParticipantLimit() == 0 || !event.getRequestModeration()
                        ? RequestState.CONFIRMED : RequestState.PENDING)
                .build();

        if (request.getStatus() == RequestState.CONFIRMED) {
            int confirmedRequests = event.getConfirmedRequests() + 1;
            event.setConfirmedRequests(confirmedRequests);
            eventsRepository.save(event);
        }
        return requestsRepository.save(request);
    }

    @Override
    public List<Request> getRequestsByUser(int userId) {
        return requestsRepository.findByRequesterId(userId);
    }

    @Override
    public Request updateRequestsByUser(int requestId, int userId) {

        Request request = requestsRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request not found."));
        request.setStatus(RequestState.CANCELED);

        return requestsRepository.save(request);
    }
}
