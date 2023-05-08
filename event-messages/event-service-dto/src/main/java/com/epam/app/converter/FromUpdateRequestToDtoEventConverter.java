package com.epam.app.converter;

import com.epam.app.Event;
import com.epam.app.request.EventUpdateRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class FromUpdateRequestToDtoEventConverter implements Converter<EventUpdateRequest, Event> {
    @Override
    public @NonNull Event convert(@NonNull EventUpdateRequest source) {
        var event = new Event();
        event.setEventType(source.getEventType());
        event.setTitle(source.getTitle());
        event.setPlace(source.getPlace());
        event.setSpeaker(source.getSpeaker());
        return event;
    }
}
