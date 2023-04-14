package com.epam.app.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
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
    public ConnectionFactory connectionFactory() {
        var connectionFactory = new CachingConnectionFactory(
                rabbitMQPropertiesConfig.getRabbitMQHost(),
                rabbitMQPropertiesConfig.getRabbitMQPort());

        connectionFactory.setUsername(rabbitMQPropertiesConfig.getRabbitMQUsername());
        connectionFactory.setPassword(rabbitMQPropertiesConfig.getRabbitMQPassword());
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin admin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
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
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", rabbitMQPropertiesConfig.getRabbitMQQueueDlqCreateRequestName())
                .build();
    }

    @Bean
    public Queue createUpdateRequestQueue() {
        return QueueBuilder.nonDurable(rabbitMQPropertiesConfig.getRabbitMQQueueUpdateRequestName())
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", rabbitMQPropertiesConfig.getRabbitMQQueueDlqUpdateRequestName())
                .build();
    }

    @Bean
    public Queue createDeleteRequestQueue() {
        return QueueBuilder.nonDurable(rabbitMQPropertiesConfig.getRabbitMQQueueDeleteRequestName())
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", rabbitMQPropertiesConfig.getRabbitMQQueueDlqDeleteRequestName())
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
