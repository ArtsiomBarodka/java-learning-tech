package com.epam.app.consumer;

import com.epam.app.Event;
import com.epam.app.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Profile("activemq")
@Slf4j
@Component
@RequiredArgsConstructor
public class ActiveMQEventConsumer {
    private final EventService eventService;

    private final RetryTemplate jmsRetryTemplate;

    @JmsListener(containerFactory = "jmsListenerRequestEventContainerFactory",
            destination = "${activemq.queue.create-request.name}")
    public void createEventRequest(Event event, @Header(JmsHeaders.DESTINATION) String destination) {
        jmsRetryTemplate.execute(arg -> {
            log.info("Create Event is received: destination = {}, event = {}", destination, event);

            eventService.createEvent(event);
            return null;
        });
    }

    @JmsListener(containerFactory = "jmsListenerRequestEventContainerFactory",
            destination = "${activemq.queue.update-request.name}")
    public void updateEventRequest(Event event, @Header(JmsHeaders.DESTINATION) String destination) {
        jmsRetryTemplate.execute(arg -> {
            log.info("Update Event is received: destination = {}, event = {}", destination, event);

            eventService.updateEvent(event.getEventId(), event);
            return null;
        });
    }

    @JmsListener(containerFactory = "jmsListenerDeleteRequestEventContainerFactory",
            destination = "${activemq.queue.delete-request.name}")
    public void deleteEventRequest(Long eventId, @Header(JmsHeaders.DESTINATION) String destination) {
        jmsRetryTemplate.execute(arg -> {
            log.info("Delete Event is received: destination = {}, event id = {}", destination, eventId);

            eventService.deleteEvent(eventId);
            return null;
        });
    }
}
