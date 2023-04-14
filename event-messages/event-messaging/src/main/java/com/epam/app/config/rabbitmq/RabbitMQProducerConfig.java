package com.epam.app.config.rabbitmq;

import com.epam.app.exception.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.TimeUnit;

@Profile("rabbitmq")
@Configuration
@RequiredArgsConstructor
public class RabbitMQProducerConfig {
    private final RabbitMQPropertiesConfig rabbitMQPropertiesConfig;

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    private RetryTemplate getRetryTemplate() {
        return RetryTemplate.builder()
                .fixedBackoff(TimeUnit.SECONDS.toMillis(rabbitMQPropertiesConfig.getRabbitMQProducerRetryDelay()))
                .maxAttempts(rabbitMQPropertiesConfig.getRabbitMQProducerRetryAttempts())
                .retryOn(ObjectNotFoundException.class).build();
    }

    @Bean
    public RabbitTemplate eventRabbitTemplate(ConnectionFactory connectionFactory) {
        var template = new RabbitTemplate(connectionFactory);
        template.setRetryTemplate(getRetryTemplate());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public RabbitTemplate deleteRabbitTemplate(ConnectionFactory connectionFactory) {
        var template = new RabbitTemplate(connectionFactory);
        template.setRetryTemplate(getRetryTemplate());
        return template;
    }
}
