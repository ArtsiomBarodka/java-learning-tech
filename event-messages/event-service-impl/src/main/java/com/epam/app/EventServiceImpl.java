package com.epam.app;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.repository.EventRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired(required = false)
    private EventMessaging eventMessaging;


    @Override
    public @NonNull Event createEvent(@NonNull Event event) {
        final var createdEvent = eventRepository.createEvent(event);

        if (eventMessaging != null) {
            eventMessaging.createEvent(createdEvent);
        }

        log.info("The New Event was created. Created Event: {}", createdEvent);

        return createdEvent;
    }

    @Override
    public @NonNull Event updateEvent(@NonNull Long id, @NonNull Event event) {
        final var storedEvent = eventRepository.getEvent(id)
                .orElseThrow(throwEventNotFoundException(id));

        updateEventFields(storedEvent, event);
        final var updatedEvent = eventRepository.updateEvent(id, storedEvent);
        if (eventMessaging != null) {
            eventMessaging.updateEvent(updatedEvent);
        }

        log.info("The Event with id = {} was updated. Updated Event: {}", id, updatedEvent);

        return updatedEvent;
    }

    @Override
    public @NonNull Event getEvent(@NonNull Long id) {
        final var event = eventRepository.getEvent(id)
                .orElseThrow(throwEventNotFoundException(id));

        log.info("The Event with id = {} was retrieved. Event: {}", id, event);

        return event;
    }

    @Override
    public @NonNull Event deleteEvent(@NonNull Long id) {
        final var deletedEvent = eventRepository.deleteEvent(id)
                .orElseThrow(throwEventNotFoundException(id));

        if (eventMessaging != null) {
            eventMessaging.deleteEvent(id);
        }

        log.info("The Event with id = {} was deleted. Deleted Event: {}", id, deletedEvent);

        return deletedEvent;
    }

    @Override
    public @NonNull List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    private Supplier<ObjectNotFoundException> throwEventNotFoundException(Long eventId) {
        return () -> {
            log.warn("Event with (id = {}) is not found", eventId);
            return new ObjectNotFoundException(String.format("Event with (id = %d) is not found", eventId));
        };
    }

    private void updateEventFields(Event storedEvent, Event newEventFields) {
        if (newEventFields.getEventType() != null) {
            storedEvent.setEventType(newEventFields.getEventType());
        }

        if (newEventFields.getPlace() != null) {
            storedEvent.setPlace(newEventFields.getPlace());
        }

        if (newEventFields.getSpeaker() != null) {
            storedEvent.setSpeaker(newEventFields.getSpeaker());
        }

        if (newEventFields.getTitle() != null) {
            storedEvent.setTitle(newEventFields.getTitle());
        }
    }
}
