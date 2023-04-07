package com.epam.app;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventServiceImpl implements EventService {
    @Override
    public @NonNull Event createEvent(@NonNull Event event) {
        return null;
        ConcurrentHashMap
    }

    @Override
    public @NonNull Event updateEvent(@NonNull Long id, @NonNull Event event) {
        return null;
    }

    @Override
    public @NonNull Event getEvent(@NonNull Long id) {
        return null;
    }

    @Override
    public @NonNull Event deleteEvent(@NonNull Long id) {
        return null;
    }

    @Override
    public @NonNull List<Event> getAllEvents() {
        return null;
    }
}
