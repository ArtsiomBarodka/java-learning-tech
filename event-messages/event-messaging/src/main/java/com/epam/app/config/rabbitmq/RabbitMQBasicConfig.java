package com.epam.app.config.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.nio.ByteBuffer;

@Profile("rabbitmq")
@RequiredArgsConstructor
@Configuration
public class RabbitMQBasicConfig {
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
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public MessageConverter longMessageConverter() {
        return new MessageConverter() {
            @Override
            public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
                if (object instanceof Long value) {
                    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                    buffer.putLong(value);
                    return new Message(buffer.array(), messageProperties);
                }
                throw new MessageConversionException("Payload must be a Long");
            }

            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                byte[] body = message.getBody();
                if (body == null || body.length == 0) {
                    return null;
                }
                ByteBuffer buffer = ByteBuffer.wrap(body);
                return buffer.getLong();
            }
        };
    }
}
