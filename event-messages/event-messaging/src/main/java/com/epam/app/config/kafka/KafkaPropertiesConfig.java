package com.epam.app.config.kafka;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("kafka")
@Getter
@Component
public class KafkaPropertiesConfig {
    @Value("${kafka.bootstrapAddress}")
    private String kafkaServer;

    //topics
    //create-event
    @Value("${kafka.topic.create-event.name}")
    private String kafkaTopicCreateEventName;

    @Value("${kafka.topic.create-event.partitions}")
    private int kafkaTopicCreateEventPartitions;

    @Value("${kafka.topic.create-event.replicas}")
    private int kafkaTopicCreateEventReplicas;

    //update-event
    @Value("${kafka.topic.update-event.name}")
    private String kafkaTopicUpdateEventName;

    @Value("${kafka.topic.update-event.partitions}")
    private int kafkaTopicUpdateEventPartitions;

    @Value("${kafka.topic.update-event.replicas}")
    private int kafkaTopicUpdateEventReplicas;

    //delete-event
    @Value("${kafka.topic.delete-event.name}")
    private String kafkaTopicDeleteEventName;

    @Value("${kafka.topic.delete-event.partitions}")
    private int kafkaTopicDeleteEventPartitions;

    @Value("${kafka.topic.delete-event.replicas}")
    private int kafkaTopicDeleteEventReplicas;

    //create-request
    @Value("${kafka.topic.create-request.name}")
    private String kafkaTopicCreateRequestName;

    @Value("${kafka.topic.create-request.partitions}")
    private int kafkaTopicCreateRequestPartitions;

    @Value("${kafka.topic.create-request.replicas}")
    private int kafkaTopicCreateRequestReplicas;

    //update-request
    @Value("${kafka.topic.update-request.name}")
    private String kafkaTopicUpdateRequestName;

    @Value("${kafka.topic.update-request.partitions}")
    private int kafkaTopicUpdateRequestPartitions;

    @Value("${kafka.topic.update-request.replicas}")
    private int kafkaTopicUpdateRequestReplicas;

    //delete-request
    @Value("${kafka.topic.delete-request.name}")
    private String kafkaTopicDeleteRequestName;

    @Value("${kafka.topic.delete-request.partitions}")
    private int kafkaTopicDeleteRequestPartitions;

    @Value("${kafka.topic.delete-request.replicas}")
    private int kafkaTopicDeleteRequestReplicas;


    @Value("${kafka.consumer.request.offset}")
    private String kafkaConsumerRequestOffset;

    @Value("${kafka.consumer.request.count}")
    private int kafkaConsumerRequestCount;
}
