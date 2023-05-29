package com.epam.app.service;

import com.avro.User;
import com.epam.app.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationServiceImpl implements NotificationService {
    private final KafkaTemplate<String, User> avroKafkaTemplate;

    private final PropertiesConfig propertiesConfig;

    @Override
    public void notify(User user) {
        String topic = propertiesConfig.getKafkaTopicAvroName();
        String key = String.valueOf(user.getId());
        log.info("Sending UserV1 notification to {} topic with id {} and Message: {}", topic, key, user);
        avroKafkaTemplate.send(topic, key, user);
    }
}
