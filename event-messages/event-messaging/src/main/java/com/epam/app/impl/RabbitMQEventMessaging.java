package com.epam.app.impl;

import com.epam.app.Event;
import com.epam.app.EventMessaging;
import com.epam.app.config.rabbitmq.RabbitMQPropertiesConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("rabbitmq")
@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQEventMessaging implements EventMessaging {
    @Autowired
    private final RabbitMQPropertiesConfig rabbitMQPropertiesConfig;
    @Autowired
    private final RabbitTemplate eventRabbitTemplate;
    @Autowired
    private final RabbitTemplate deleteRabbitTemplate;

    @Override
    public void createEvent(@NonNull Event event) {
        String routingKey = rabbitMQPropertiesConfig.getRabbitMQQueueCreateEventName();

        log.info("Sending 'createEvent' notification to queue with {} routingKey. Event: {}", routingKey, event);

        eventRabbitTemplate.convertAndSend(routingKey, event);
    }

    @Override
    public void updateEvent(@NonNull Event event) {
        String routingKey = rabbitMQPropertiesConfig.getRabbitMQQueueUpdateEventName();

        log.info("Sending 'updateEvent' notification to queue with {} routingKey. Event: {}", routingKey, event);

        eventRabbitTemplate.convertAndSend(routingKey, event);
    }

    @Override
    public void deleteEvent(@NonNull Long id) {
        String routingKey = rabbitMQPropertiesConfig.getRabbitMQQueueDeleteEventName();

        log.info("Sending 'deleteEvent' notification to queue with {} routingKey. Event id: {}", routingKey, id);

        deleteRabbitTemplate.convertAndSend(routingKey, id);
    }
}
