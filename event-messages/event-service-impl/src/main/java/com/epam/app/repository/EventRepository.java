package com.epam.app.repository;

import com.epam.app.Event;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    @NonNull Event createEvent(@NonNull Event event);

    @NonNull Event updateEvent(@NonNull Long id, @NonNull Event event);

    @NonNull Optional<Event> getEvent(@NonNull Long id);

    @NonNull Optional<Event> deleteEvent(@NonNull Long id);

    @NonNull List<Event> getAllEvents();
}
