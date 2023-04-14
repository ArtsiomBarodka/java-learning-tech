package com.epam.app;

import lombok.NonNull;

public interface EventMessaging {
    void createEvent(@NonNull Event event);
    void updateEvent(@NonNull Event event);
    void deleteEvent(@NonNull Long id);
}
