package com.epam.app.impl;

import com.epam.app.Event;
import com.epam.app.EventMessaging;
import com.epam.app.config.acvtivemq.ActiveMQPropertiesConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("activemq")
@Service
@RequiredArgsConstructor
public class ActiveMQEventMessaging implements EventMessaging {

    private final ActiveMQPropertiesConfig activeMQPropertiesConfig;

    @Autowired
    private JmsTemplate eventJmsTemplate;

    @Autowired
    private JmsTemplate deleteEventJmsTemplate;

    @Override
    public void createEvent(@NonNull Event event) {
        var eventDestination = activeMQPropertiesConfig.getActiveMQQueueCreateEventName();

        log.info("Sending 'createEvent' notification to queue = {}. Event: {}", eventDestination, event);

        eventJmsTemplate.convertAndSend(eventDestination, event);
    }

    @Override
    public void updateEvent(@NonNull Event event) {
        var eventDestination = activeMQPropertiesConfig.getActiveMQQueueUpdateEventName();

        log.info("Sending 'updateEvent' notification to queue = {}. Event: {}", eventDestination, event);

        eventJmsTemplate.convertAndSend(eventDestination, event);
    }

    @Override
    public void deleteEvent(@NonNull Long id) {
        var eventDestination = activeMQPropertiesConfig.getActiveMQQueueDeleteEventName();

        log.info("Sending 'deleteEvent' notification to queue = {}. Event id: {}", eventDestination, id);

        deleteEventJmsTemplate.convertAndSend(eventDestination, id);
    }
}
