package com.epam.app.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("rabbitmq")
@RequiredArgsConstructor
@Configuration
public class RabbitMQQueuesConfig {
    private final RabbitMQPropertiesConfig rabbitMQPropertiesConfig;

    @Bean
    public RabbitAdmin admin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue createCreateEventQueue() {
        return new Queue(rabbitMQPropertiesConfig.getRabbitMQQueueCreateEventName(), false);
    }

    @Bean
    public Queue createUpdateEventQueue() {
        return new Queue(rabbitMQPropertiesConfig.getRabbitMQQueueUpdateEventName(), false);
    }

    @Bean
    public Queue createDeleteEventQueue() {
        return new Queue(rabbitMQPropertiesConfig.getRabbitMQQueueDeleteEventName(), false);
    }

    @Bean
    public Queue createCreateRequestQueue() {
        return QueueBuilder.nonDurable(rabbitMQPropertiesConfig.getRabbitMQQueueCreateRequestName())
                .deadLetterExchange("")
                .deadLetterRoutingKey(rabbitMQPropertiesConfig.getRabbitMQQueueDlqCreateRequestName())
                .build();
    }

    @Bean
    public Queue createUpdateRequestQueue() {
        return QueueBuilder.nonDurable(rabbitMQPropertiesConfig.getRabbitMQQueueUpdateRequestName())
                .deadLetterExchange("")
                .deadLetterRoutingKey(rabbitMQPropertiesConfig.getRabbitMQQueueDlqUpdateRequestName())
                .build();
    }

    @Bean
    public Queue createDeleteRequestQueue() {
        return QueueBuilder.nonDurable(rabbitMQPropertiesConfig.getRabbitMQQueueDeleteRequestName())
                .deadLetterExchange("")
                .deadLetterRoutingKey(rabbitMQPropertiesConfig.getRabbitMQQueueDlqDeleteRequestName())
                .build();
    }

    @Bean
    public Queue createDlqUpdateRequestQueue() {
        return new Queue(rabbitMQPropertiesConfig.getRabbitMQQueueDlqUpdateRequestName(), false);
    }

    @Bean
    public Queue createDlqCreateRequestQueue() {
        return new Queue(rabbitMQPropertiesConfig.getRabbitMQQueueDlqCreateRequestName(), false);
    }

    @Bean
    public Queue createDlqDeleteRequestQueue() {
        return new Queue(rabbitMQPropertiesConfig.getRabbitMQQueueDlqDeleteRequestName(), false);
    }
}
