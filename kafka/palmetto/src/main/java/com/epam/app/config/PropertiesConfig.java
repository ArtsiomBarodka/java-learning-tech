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

    @Value("${kafka.topic.order.partitions}")
    private int kafkaTopicOrderPartitions;

    @Value("${kafka.topic.order.replicas}")
    private int kafkaTopicOrderReplicas;

    @Value("${kafka.topic.notification.name}")
    private String kafkaTopicNotificationName;

    @Value("${kafka.topic.notification.partitions}")
    private int kafkaTopicNotificationPartitions;

    @Value("${kafka.topic.notification.replicas}")
    private int kafkaTopicNotificationReplicas;

    @Value("${kafka.consumer.order.offset}")
    private String kafkaConsumerOrderOffset;

    @Value("${kafka.consumer.order.count}")
    private int kafkaConsumerOrderCount;

    @Value("${kafka.consumer.order.retry.interval}")
    private int kafkaConsumerOrderRetryInterval;

    @Value("${kafka.consumer.order.retry.attempts}")
    private int kafkaConsumerOrderRetryAttempts;

    @Value("${kafka.consumer.order.palmetto.group.id}")
    private String kafkaConsumerOrderPalmettoGroupId;
}
