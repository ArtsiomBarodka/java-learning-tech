package com.epam.app.request;

import com.epam.app.EventType;
import lombok.Data;

@Data
public class EventUpdateRequest {
    private String title;
    private String place;
    private String speaker;
    private EventType eventType;
}
