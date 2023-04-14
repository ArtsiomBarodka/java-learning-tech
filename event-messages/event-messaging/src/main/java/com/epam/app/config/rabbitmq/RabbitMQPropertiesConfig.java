package com.epam.app.config.rabbitmq;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("rabbitmq")
@Getter
@Component
public class RabbitMQPropertiesConfig {
    @Value("${rabbit.host}")
    private String rabbitMQHost;

    @Value("${rabbit.port}")
    private int rabbitMQPort;

    @Value("${rabbit.username}")
    private String rabbitMQUsername;

    @Value("${rabbit.password}")
    private String rabbitMQPassword;

    //queues
    //create-event
    @Value("${rabbit.queue.create-event.name}")
    private String rabbitMQQueueCreateEventName;

    //update-event
    @Value("${rabbit.queue.update-event.name}")
    private String rabbitMQQueueUpdateEventName;

    //delete-event
    @Value("${rabbit.queue.delete-event.name}")
    private String rabbitMQQueueDeleteEventName;

    //create-request
    @Value("${rabbit.queue.create-request.name}")
    private String rabbitMQQueueCreateRequestName;

    //update-request
    @Value("${rabbit.queue.update-request.name}")
    private String rabbitMQQueueUpdateRequestName;

    //delete-request
    @Value("${rabbit.queue.delete-request.name}")
    private String rabbitMQQueueDeleteRequestName;

    //dlq-create-request
    @Value("${rabbit.queue.dlq.create-request.name}")
    private String rabbitMQQueueDlqCreateRequestName;

    //dlq-update-request
    @Value("${rabbit.queue.dlq.update-request.name}")
    private String rabbitMQQueueDlqUpdateRequestName;

    //dlq-delete-request
    @Value("${rabbit.queue.dlq.delete-request.name}")
    private String rabbitMQQueueDlqDeleteRequestName;

    //producers
    @Value("${rabbit.producer.retry.attempts}")
    private int rabbitMQProducerRetryAttempts;

    @Value("${rabbit.producer.retry.delay}")
    private int rabbitMQProducerRetryDelay;

    //consumers
    @Value("${rabbit.consumer.concurrent}")
    private int rabbitMQConsumerConcurrent;

    @Value("${rabbit.consumer.max-concurrent}")
    private int rabbitMQConsumerMaxConcurrent;


    @Value("${rabbit.consumer.retry.attempts}")
    private int rabbitMQConsumerRetryAttempts;

    @Value("${rabbit.consumer.retry.delay}")
    private int rabbitMQConsumerRetryDelay;
}
