package com.epam.app.config.rabbitmq;

import com.epam.app.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.TimeUnit;

@Profile("rabbitmq")
@Configuration
@RequiredArgsConstructor
public class RabbitMQProducerConfig {
    private final RabbitMQPropertiesConfig rabbitMQPropertiesConfig;

    private RetryTemplate getRetryTemplate() {
        return RetryTemplate.builder()
                .fixedBackoff(TimeUnit.SECONDS.toMillis(rabbitMQPropertiesConfig.getRabbitMQProducerRetryDelay()))
                .maxAttempts(rabbitMQPropertiesConfig.getRabbitMQProducerRetryAttempts())
                .retryOn(ObjectNotFoundException.class).build();
    }

    @Bean
    public RabbitTemplate eventRabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        var template = new RabbitTemplate(connectionFactory);
        template.setRetryTemplate(getRetryTemplate());
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public RabbitTemplate deleteRabbitTemplate(ConnectionFactory connectionFactory, MessageConverter longMessageConverter) {
        var template = new RabbitTemplate(connectionFactory);
        template.setRetryTemplate(getRetryTemplate());
        template.setMessageConverter(longMessageConverter);
        return template;
    }
}
