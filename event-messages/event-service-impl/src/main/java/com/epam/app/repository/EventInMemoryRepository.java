package com.epam.app.repository;

import com.epam.app.Event;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EventInMemoryRepository implements EventRepository {
    private static final AtomicLong NEW_EVENT_ID = new AtomicLong(1L);
    private static final Map<Long, Event> STORE = new ConcurrentHashMap<>();

    @Override
    public @NonNull Event createEvent(@NonNull Event event) {
        event.setEventId(NEW_EVENT_ID.getAndIncrement());
        event.setDateTime(LocalDateTime.now());
        STORE.put(event.getEventId(), event);
        return event;
    }

    @Override
    public @NonNull Event updateEvent(@NonNull Long id, @NonNull Event event) {
        STORE.put(id, event);
        return event;
    }

    @Override
    public @NonNull Optional<Event> getEvent(@NonNull Long id) {
        return Optional.ofNullable(STORE.get(id));
    }

    @Override
    public @NonNull Optional<Event> deleteEvent(@NonNull Long id) {
        return Optional.ofNullable(STORE.remove(id));
    }

    @Override
    public @NonNull List<Event> getAllEvents() {
        return new ArrayList<>(STORE.values());
    }
}
