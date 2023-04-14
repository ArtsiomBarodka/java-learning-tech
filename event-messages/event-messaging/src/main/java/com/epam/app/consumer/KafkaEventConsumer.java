package com.epam.app.consumer;

import com.epam.app.Event;
import com.epam.app.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {
    private final EventService eventService;

    @KafkaListener(topics = "#{'${kafka.topic.create-request.name}'.split(',')}",
            groupId = "${kafka.consumer.request.group.id}",
            containerFactory = "requestEventKafkaListenerContainerFactory")
    public void createEventRequest(Event event,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                   @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("Create Event is received: topic = {}, key = {}, event = {}", topic, key, event);
        eventService.createEvent(event);
    }

    @KafkaListener(topics = "#{'${kafka.topic.update-request.name}'.split(',')}",
            groupId = "${kafka.consumer.request.group.id}",
            containerFactory = "requestEventKafkaListenerContainerFactory")
    public void updateEventRequest(Event event,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                   @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("Update Event is received: topic = {}, key = {}, event = {}", topic, key, event);
        eventService.updateEvent(event.getEventId(), event);
    }

    @KafkaListener(topics = "#{'${kafka.topic.delete-request.name}'.split(',')}",
            groupId = "${kafka.consumer.request.group.id}",
            containerFactory = "deleteRequestEventKafkaListenerContainerFactory")
    public void deleteEventRequest(Long id,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                   @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("Delete Event is received: topic = {}, key = {}, event id = {}", topic, key, id);
        eventService.deleteEvent(id);
    }
}
