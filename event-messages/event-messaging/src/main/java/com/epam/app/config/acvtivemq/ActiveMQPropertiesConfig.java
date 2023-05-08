package com.epam.app.config.acvtivemq;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("activemq")
@Getter
@Component
public class ActiveMQPropertiesConfig {
    @Value("${activemq.url}")
    private String activeMQUrl;

    @Value("${activemq.username}")
    private String activeMQUsername;

    @Value("${activemq.password}")
    private String activeMQPassword;

    @Value("${activemq.queue.create-event.name}")
    private String activeMQQueueCreateEventName;

    @Value("${activemq.queue.update-event.name}")
    private String activeMQQueueUpdateEventName;

    @Value("${activemq.queue.delete-event.name}")
    private String activeMQQueueDeleteEventName;

    @Value("${activemq.queue.create-request.name}")
    private String activeMQQueueCreateRequestName;

    @Value("${activemq.queue.update-request.name}")
    private String activeMQQueueUpdateRequestName;

    @Value("${activemq.queue.delete-request.name}")
    private String activeMQQueueDeleteRequestName;

    @Value("${activemq.consumer.concurrency.min}")
    private int activeMQConsumerConcurrencyMin;

    @Value("${activemq.consumer.concurrency.max}")
    private int activeMQConsumerConcurrencyMax;

    @Value("${activemq.consumer.retry.attempts}")
    private int activeMQConsumerRetryAttempts;

    @Value("${activemq.consumer.retry.delay}")
    private int activeMQConsumerRetryDelay;
}
