package com.epam.app.impl;

import com.epam.app.Event;
import com.epam.app.EventMessaging;
import com.epam.app.config.kafka.KafkaPropertiesConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("kafka")
@Service
@RequiredArgsConstructor
public class KafkaEventMessaging implements EventMessaging {

    private final KafkaPropertiesConfig kafkaPropertiesConfig;

    private final KafkaTemplate<String, Event> eventKafkaTemplate;

    private final KafkaTemplate<String, Long> deleteEventKafkaTemplate;

    @Override
    public void createEvent(@NonNull Event event) {
        String topic = kafkaPropertiesConfig.getKafkaTopicCreateEventName();
        String key = String.valueOf(event.getEventId());

        log.info("Sending 'createEvent' notification to {} topic and {} key . Event: {}", topic, key, event);

        eventKafkaTemplate.send(topic, key, event);
    }

    @Override
    public void updateEvent(@NonNull Event event) {
        String topic = kafkaPropertiesConfig.getKafkaTopicUpdateEventName();
        String key = String.valueOf(event.getEventId());

        log.info("Sending 'updateEvent' notification to {} topic and {} key . Event: {}", topic, key, event);

        eventKafkaTemplate.send(topic, key, event);
    }

    @Override
    public void deleteEvent(@NonNull Long id) {
        String topic = kafkaPropertiesConfig.getKafkaTopicDeleteEventName();
        String key = String.valueOf(id);

        log.info("Sending 'deleteEvent' notification to {} topic and {} key . Event id: {}", topic, key, id);

        deleteEventKafkaTemplate.send(topic, key, id);
    }
}
