package com.epam.app.converter;

import com.epam.app.Event;
import com.epam.app.request.EventCreateRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class FromCreateRequestToDtoEventConverter implements Converter<Event, EventCreateRequest> {
    @Override
    public @NonNull Event convert(@NonNull EventCreateRequest source) {
        var event = new Event();
        event.setEventType(source.getEventType());
        event.setTitle(source.getTitle());
        event.setPlace(source.getPlace());
        event.setSpeaker(source.getSpeaker());
        return event;
    }
}
