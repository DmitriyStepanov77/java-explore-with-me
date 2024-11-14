package ru.practicum.explore.service.event;

import com.querydsl.core.BooleanBuilder;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.StatClient;
import ru.practicum.explore.StatDto;
import ru.practicum.explore.dto.event.*;
import ru.practicum.explore.dto.request.RequestUpdateDto;
import ru.practicum.explore.exception.*;
import ru.practicum.explore.mapper.NewEventDtoMapper;
import ru.practicum.explore.mapper.RequestDtoMapper;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.QEvent;
import ru.practicum.explore.model.Request;
import ru.practicum.explore.model.enums.EventState;
import ru.practicum.explore.model.enums.RequestState;
import ru.practicum.explore.repository.EventsRepository;
import ru.practicum.explore.repository.RequestsRepository;
import ru.practicum.explore.service.category.CategoryService;
import ru.practicum.explore.service.user.UserService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@AllArgsConstructor
public class EventServiceImp implements EventService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventsRepository eventsRepository;
    private final NewEventDtoMapper newEventDtoMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestsRepository requestsRepository;
    private final RequestDtoMapper requestDtoMapper;
    private final StatClient statClient;

    @Override
    public Event addEvent(NewEventDto newEventDto, int userId) {
        Event event = newEventDtoMapper.toEntity(newEventDto);

        log.info("Added event with title = {} and event date = {}.", event.getTitle(), event.getEventDate());

        event.setInitiator(userService.getUser(userId));
        log.info("Initiator event = {}.", event.getInitiator().getName());
        event.setCategory(categoryService.getCategory(newEventDto.getCategory()));
        log.info("Category event = {}.", event.getCategory().getName());
        event.setCreatedOn(LocalDateTime.now());
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setViews(0L);
        event.setConfirmedRequests(0);
        return eventsRepository.save(event);
    }

    @Override
    public Event updateUserEvent(UpdateUserEventDto eventDto, int eventId, int userId) {

        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found."));
        checkInitiatorEvent(userId, event.getInitiator().getId());
        if (event.getState() == EventState.PUBLISHED)
            throw new UpdateEventIsPublishedException("Update event is published.");
        else if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now().plusMinutes(119)))
            throw new UpdateEventTimeIncorrectException("Update event time = " + eventDto.getEventDate() + " is incorrect.");

        log.info("Update event with id = {} by user.", event.getId());

        if (eventDto.getStateAction() != null) {
            switch (eventDto.getStateAction()) {
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
                case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
                default -> throw new ConflictException("State event is incorrect.");
            }
        }

        log.info("New state event = {}.", event.getState());

        return eventsRepository.save(updateEvent(event, eventDto.getCategory(), eventDto.getAnnotation(),
                eventDto.getDescription(), eventDto.getLocation(), eventDto.getEventDate(), eventDto.getPaid(),
                eventDto.getParticipantLimit(), eventDto.getRequestModeration(), eventDto.getTitle()));
    }

    @Override
    public Event updateAdminEvent(UpdateAdminEventDto eventDto, int eventId) {

        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found."));
        if (event.getState() != EventState.PENDING)
            throw new UpdateEventIsPublishedException("Update event state is incorrect.");
        else if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now().plusMinutes(59)))
            throw new UpdateEventTimeIncorrectException("Update event time = " + eventDto.getEventDate() + " is incorrect.");

        log.info("Update event with id = {} by admin.", event.getId());

        if (eventDto.getStateAction() != null) {
            switch (eventDto.getStateAction()) {
                case REJECT_EVENT -> event.setState(EventState.CANCELED);
                case PUBLISH_EVENT -> event.setState(EventState.PUBLISHED);
                default -> throw new ConflictException("State event is incorrect.");
            }
        }

        log.info("New state event = {}.", event.getState());

        return eventsRepository.save(updateEvent(event, eventDto.getCategory(), eventDto.getAnnotation(),
                eventDto.getDescription(), eventDto.getLocation(), eventDto.getEventDate(), eventDto.getPaid(),
                eventDto.getParticipantLimit(), eventDto.getRequestModeration(), eventDto.getTitle()));
    }

    @Override
    public Event getEvent(int eventId, boolean publicState) {

        log.info("Get event with id = {}.", eventId);

        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event is not found."));
        if (publicState && event.getState() != EventState.PUBLISHED)
            throw new NotFoundException("Event is not published.");
        setViews(List.of(event));
        return eventsRepository.save(event);
    }

    @Override
    public List<Event> getEventsByUser(int initiatorId, int from, int size) {

        log.info("Get event by user with id = {}.", initiatorId);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Event> events = eventsRepository.findByInitiatorId(initiatorId, pageable);
        setViews(events);
        return eventsRepository.saveAll(events);
    }

    @Override
    public List<Event> getEventsWithFilter(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                           String rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        log.info("Get events with filter.");

        String sorted = "id";
        if (sort != null && sort.equals("VIEWS"))
            sorted = "views";
        else if (sort != null && sort.equals("EVENT_DATE"))
            sorted = "eventDate";

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, sorted));

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (categories != null && !categories.isEmpty())
            booleanBuilder.and(QEvent.event.category.id.in(categories));
        if (paid != null)
            booleanBuilder.and(QEvent.event.paid.eq(paid));
        if (onlyAvailable)
            booleanBuilder.and(QEvent.event.participantLimit.eq(0)
                    .or(QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit)));
        if (text != null)
            booleanBuilder.and(QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text)));
        booleanBuilder = checkTime(booleanBuilder, rangeStart, rangeEnd);
        return eventsRepository.findAll(booleanBuilder, pageable).getContent();
    }

    @Override
    public List<Event> getEventsSearch(List<Integer> users, List<String> states, List<Integer> categories,
                                       String rangeStart, String rangeEnd, int from, int size) {

        log.info("Get events with search.");

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (users != null && !users.isEmpty())
            booleanBuilder.and(QEvent.event.initiator.id.in(users));
        if (states != null && !states.isEmpty())
            booleanBuilder.and(QEvent.event.state.in(states.stream().map(EventState::convert).toList()));
        if (categories != null && !categories.isEmpty())
            booleanBuilder.and(QEvent.event.category.id.in(categories));
        booleanBuilder = checkTime(booleanBuilder, rangeStart, rangeEnd);
        return eventsRepository.findAll(booleanBuilder, pageable).getContent();
    }

    @Override
    public List<Request> getRequestsByEvent(int userId, int eventId) {
        log.info("Get requests by event with id = {}.", eventId);

        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found."));
        checkInitiatorEvent(userId, event.getInitiator().getId());
        return requestsRepository.findByEventId(eventId);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(RequestUpdateDto updateDto, int userId, int eventId) {

        log.info("Update request status for event with id = {}.", eventId);

        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found."));
        checkInitiatorEvent(userId, event.getInitiator().getId());
        List<Request> requests = requestsRepository.findAllByIdIn(updateDto.getRequestIds());

        int confirmedRequest = event.getConfirmedRequests();
        if (confirmedRequest >= event.getParticipantLimit())
            throw new ConflictException("Participants limit exceeded");

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        for (Request r : requests) {
            if (r.getStatus() != RequestState.PENDING)
                throw new ConflictException("The request is not in pending status.");
            if (updateDto.getStatus() == RequestState.CONFIRMED && confirmedRequest < event.getParticipantLimit()) {
                r.setStatus(RequestState.CONFIRMED);
                confirmedRequest++;
                confirmedRequests.add(r);
                log.info("Request with id = {} update status = {}.", r.getId(), r.getStatus());
            } else {
                r.setStatus(RequestState.REJECTED);
                rejectedRequests.add(r);
                log.info("Request with id = {} update status = {}.", r.getId(), r.getStatus());
            }
        }

        log.info("Event with id = {} confirmed request = {}.", event.getId(), event.getConfirmedRequests());

        event.setConfirmedRequests(confirmedRequest);
        eventsRepository.save(event);
        requestsRepository.saveAll(confirmedRequests);
        requestsRepository.saveAll(rejectedRequests);

        return new EventRequestStatusUpdateResult(confirmedRequests.stream().map(requestDtoMapper::toModel).toList(),
                rejectedRequests.stream().map(requestDtoMapper::toModel).toList());
    }

    private Event updateEvent(Event updateEvent, Integer categoryId, String annotation, String description,
                              LocationDto location, LocalDateTime eventDate, Boolean paid,
                              Integer participantLimit, Boolean requestModeration, String title) {

        if (categoryId != null) {
            updateEvent.setCategory(categoryService.getCategory(categoryId));
            log.info("New category event = {}.", updateEvent.getCategory().getName());
        }
        if (location != null) {
            updateEvent.setLocationLon(location.getLon());
            updateEvent.setLocationLat(location.getLat());
            log.info("New location event: lon = {}, lat = {}.", location.getLon(), location.getLat());
        }

        Optional.ofNullable(annotation).ifPresent(updateEvent::setAnnotation);
        Optional.ofNullable(description).ifPresent(updateEvent::setDescription);
        Optional.ofNullable(eventDate).ifPresent(updateEvent::setEventDate);
        Optional.ofNullable(paid).ifPresent(updateEvent::setPaid);
        Optional.ofNullable(participantLimit).ifPresent(updateEvent::setParticipantLimit);
        Optional.ofNullable(requestModeration).ifPresent(updateEvent::setRequestModeration);
        Optional.ofNullable(title).ifPresent(updateEvent::setTitle);

        return updateEvent;
    }

    private void checkInitiatorEvent(int userId, int initiatorId) {
        if (initiatorId != userId)
            throw new UserIsNotInitiatorException("User is not initiator event.");
    }

    private BooleanBuilder checkTime(BooleanBuilder booleanBuilder, String rangeStart, String rangeEnd) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null && !rangeStart.isEmpty()) {
            start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8), formatter);
            booleanBuilder.and(QEvent.event.eventDate.after(start));
        }
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8), formatter);
            booleanBuilder.and(QEvent.event.eventDate.before(end));
        }
        if (start != null && end != null && start.isAfter(end))
            throw new ValidationException("Time is incorrect.");
        return booleanBuilder;
    }

    private List<StatDto> getViews(List<Event> events) {
        List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();
        List<StatDto> viewStats = statClient.getStat(LocalDateTime.now().minusMonths(1),
                LocalDateTime.now(), uris, true);

        if (viewStats == null) {
            return Collections.emptyList();
        }
        return viewStats;
    }

    private void setViews(List<Event> events) {
        if (events.isEmpty()) {
            return;
        }
        Map<String, Long> mapUriAndHits = getViews(events).stream()
                .collect(Collectors.toMap(StatDto::getUri, StatDto::getHits));

        for (Event event : events) {
            event.setViews(mapUriAndHits.getOrDefault("/events/" + event.getId(), 0L));
        }
    }

}
