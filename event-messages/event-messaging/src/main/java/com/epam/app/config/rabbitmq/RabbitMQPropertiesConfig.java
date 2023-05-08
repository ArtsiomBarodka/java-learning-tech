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

    @Value("${rabbit.queue.create-event.name}")
    private String rabbitMQQueueCreateEventName;

    @Value("${rabbit.queue.update-event.name}")
    private String rabbitMQQueueUpdateEventName;

    @Value("${rabbit.queue.delete-event.name}")
    private String rabbitMQQueueDeleteEventName;

    @Value("${rabbit.queue.create-request.name}")
    private String rabbitMQQueueCreateRequestName;

    @Value("${rabbit.queue.update-request.name}")
    private String rabbitMQQueueUpdateRequestName;

    @Value("${rabbit.queue.delete-request.name}")
    private String rabbitMQQueueDeleteRequestName;

    @Value("${rabbit.queue.dlq.create-request.name}")
    private String rabbitMQQueueDlqCreateRequestName;

    @Value("${rabbit.queue.dlq.update-request.name}")
    private String rabbitMQQueueDlqUpdateRequestName;

    @Value("${rabbit.queue.dlq.delete-request.name}")
    private String rabbitMQQueueDlqDeleteRequestName;

    @Value("${rabbit.producer.retry.attempts}")
    private int rabbitMQProducerRetryAttempts;

    @Value("${rabbit.producer.retry.delay}")
    private int rabbitMQProducerRetryDelay;

    @Value("${rabbit.consumer.concurrent}")
    private int rabbitMQConsumerConcurrent;

    @Value("${rabbit.consumer.max-concurrent}")
    private int rabbitMQConsumerMaxConcurrent;

    @Value("${rabbit.consumer.retry.attempts}")
    private int rabbitMQConsumerRetryAttempts;

    @Value("${rabbit.consumer.retry.delay}")
    private int rabbitMQConsumerRetryDelay;
}
