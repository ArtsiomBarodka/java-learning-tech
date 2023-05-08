package com.epam.app;

import lombok.NonNull;

import java.util.List;

public interface EventService {
    @NonNull Event createEvent(@NonNull Event event);

    @NonNull Event updateEvent(@NonNull Long id, @NonNull Event event);

    @NonNull Event getEvent(@NonNull Long id);

    @NonNull Event deleteEvent(@NonNull Long id);

    @NonNull List<Event> getAllEvents();
}
