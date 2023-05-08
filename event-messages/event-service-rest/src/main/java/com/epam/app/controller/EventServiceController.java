package com.epam.app.controller;

import com.epam.app.Event;
import com.epam.app.EventService;
import com.epam.app.converter.FromCreateRequestToDtoEventConverter;
import com.epam.app.converter.FromUpdateRequestToDtoEventConverter;
import com.epam.app.request.EventCreateRequest;
import com.epam.app.request.EventUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventServiceController {
    private final EventService eventService;
    private final FromCreateRequestToDtoEventConverter fromCreateRequestToDtoEventConverter;
    private final FromUpdateRequestToDtoEventConverter fromUpdateRequestToDtoEventConverter;

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        var event = eventService.getEvent(id);

        log.info("Event response for getting request by (id = {}). Event: {}", id, event);

        return ResponseEntity.ok(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        var events = eventService.getAllEvents();

        log.info("All Events response for getting request. Events: {}", events);

        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventCreateRequest eventCreateRequest) {
        var convertedEvent = fromCreateRequestToDtoEventConverter.convert(eventCreateRequest);
        var createdEvent = eventService.createEvent(convertedEvent);

        log.info("Event response for creating request. New Event: {}", createdEvent);

        return new ResponseEntity<>(convertedEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateFullyEvent(@PathVariable Long id, @RequestBody EventUpdateRequest eventUpdateRequest) {
        var convertedEvent = fromUpdateRequestToDtoEventConverter.convert(eventUpdateRequest);
        var updatedEvent = eventService.updateEvent(id, convertedEvent);

        log.info("Event response for full-updating request. Updated Event: {}", updatedEvent);

        return ResponseEntity.ok(updatedEvent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Event> updatePartiallyEvent(@PathVariable Long id, @RequestBody EventUpdateRequest eventUpdateRequest) {
        var convertedEvent = fromUpdateRequestToDtoEventConverter.convert(eventUpdateRequest);
        var updatedEvent = eventService.updateEvent(id, convertedEvent);

        log.info("Event response for partially-updating request. Updated Event: {}", updatedEvent);

        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable Long id) {
        var deletedEvent = eventService.deleteEvent(id);

        log.info("Event response for deleting request. Deleted Event: {}", deletedEvent);

        return ResponseEntity.ok(deletedEvent);
    }
}
