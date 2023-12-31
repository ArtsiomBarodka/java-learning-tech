package com.epam.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertiesConfig {
    @Value("${kafka.bootstrapAddress}")
    private String kafkaServer;

    @Value("${kafka.topic.order.name}")
    private String kafkaTopicOrderName;

    @Value("${kafka.topic.notification.name}")
    private String kafkaTopicNotificationName;

    @Value("${kafka.consumer.notification.offset}")
    private String kafkaConsumerNotificationOffset;

    @Value("${kafka.consumer.notification.count}")
    private int kafkaConsumerNotificationCount;

    @Value("${kafka.consumer.notification.client.group.id}")
    private String kafkaConsumerNotificationCourierGroupId;
}
