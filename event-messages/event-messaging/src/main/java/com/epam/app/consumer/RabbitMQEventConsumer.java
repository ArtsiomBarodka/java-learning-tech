package com.epam.app.consumer;

import com.epam.app.Event;
import com.epam.app.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Profile("rabbitmq")
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQEventConsumer {
    private final EventService eventService;

    @RabbitListener(containerFactory = "rabbitListenerRequestEventContainerFactory",
            queues = "#{'${rabbit.queue.create-request.name}'.split(',')}")
    public void createEventRequest(Event event,
                                   @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
                                   @Header(AmqpHeaders.CONSUMER_QUEUE) String queue) {

        log.info("Create Event is received: queue = {}, routingKey = {}, event = {}", queue, routingKey, event);

        eventService.createEvent(event);
    }

    @RabbitListener(containerFactory = "rabbitListenerRequestEventContainerFactory",
            queues = "#{'${rabbit.queue.update-request.name}'.split(',')}")
    public void updateEventRequest(Event event,
                                   @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
                                   @Header(AmqpHeaders.CONSUMER_QUEUE) String queue) {

        log.info("Update Event is received: queue = {}, routingKey = {}, event = {}", queue, routingKey, event);

        eventService.updateEvent(event.getEventId(), event);
    }

    @RabbitListener(containerFactory = "rabbitListenerDeleteRequestEventContainerFactory",
            queues = "#{'${rabbit.queue.delete-request.name}'.split(',')}")
    public void deleteEventRequest(Long eventId,
                                   @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
                                   @Header(AmqpHeaders.CONSUMER_QUEUE) String queue) {

        log.info("Delete Event is received: queue = {}, routingKey = {}, event id = {}", queue, routingKey, eventId);

        eventService.deleteEvent(eventId);
    }
}
