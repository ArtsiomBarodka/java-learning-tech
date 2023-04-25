package com.epam.app.impl;

import com.epam.app.Event;
import com.epam.app.EventType;
import com.epam.app.config.rabbitmq.RabbitMQBasicConfig;
import com.epam.app.config.rabbitmq.RabbitMQProducerConfig;
import com.epam.app.config.rabbitmq.RabbitMQPropertiesConfig;
import com.epam.app.config.rabbitmq.RabbitMQQueuesConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("rabbitmq")
@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {RabbitMQQueuesConfig.class, RabbitMQEventMessaging.class, RabbitMQBasicConfig.class, RabbitMQProducerConfig.class, RabbitMQPropertiesConfig.class})
public class RabbitMQEventMessagingTest {
    private static final String CREATE_EVENT_QUEUE = "createEvent-testQueue";
    private static final String UPDATE_EVENT_QUEUE = "updateEvent-testQueue";
    private static final String DELETE_EVENT_QUEUE = "deleteEvent-testQueue";

    @Container
    private static final RabbitMQContainer RABBIT_MQ =
            new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.11.13-management-alpine"));

    @DynamicPropertySource
    static void registerRabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("rabbit.host", RABBIT_MQ::getHost);
        registry.add("rabbit.port", RABBIT_MQ::getAmqpPort);
        registry.add("rabbit.username", RABBIT_MQ::getAdminUsername);
        registry.add("rabbit.password", RABBIT_MQ::getAdminPassword);
        registry.add("rabbit.queue.create-event.name", () -> CREATE_EVENT_QUEUE);
        registry.add("rabbit.queue.update-event.name", () -> UPDATE_EVENT_QUEUE);
        registry.add("rabbit.queue.delete-event.name", () -> DELETE_EVENT_QUEUE);
    }

    @Autowired
    private RabbitMQEventMessaging rabbitMQEventMessaging;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private MessageConverter longMessageConverter;

    @Test
    void createEventTest() throws IOException {
        // Arrange
        var event = new Event(1L, "title", "place", "speaker", EventType.WORKSHOP, LocalDateTime.now());

        // Act
        rabbitMQEventMessaging.createEvent(event);

        // Assert
        try (Connection connection = connectionFactory.createConnection()) {
            var channel = connection.createChannel(false);
            var getResponse = channel.basicGet(CREATE_EVENT_QUEUE, true);

            Event response = getObjectMapper().readValue(getResponse.getBody(), Event.class);

            assertThat(response.getEventId()).isEqualTo(event.getEventId());
            assertThat(response.getTitle()).isEqualTo(event.getTitle());
            assertThat(response.getSpeaker()).isEqualTo(event.getSpeaker());
            assertThat(response.getPlace()).isEqualTo(event.getPlace());
            assertThat(response.getEventType()).isEqualTo(event.getEventType());
            assertThat(response.getDateTime()).isEqualTo(event.getDateTime());
        }
    }

    @Test
    void updateEventTest() throws IOException {
        // Arrange
        var event = new Event(1L, "title", "place", "speaker", EventType.WORKSHOP, LocalDateTime.now());

        // Act
        rabbitMQEventMessaging.updateEvent(event);

        // Assert
        try (Connection connection = connectionFactory.createConnection()) {
            var channel = connection.createChannel(false);
            var getResponse = channel.basicGet(UPDATE_EVENT_QUEUE, true);

            Event response = getObjectMapper().readValue(getResponse.getBody(), Event.class);

            assertThat(response.getEventId()).isEqualTo(event.getEventId());
            assertThat(response.getTitle()).isEqualTo(event.getTitle());
            assertThat(response.getSpeaker()).isEqualTo(event.getSpeaker());
            assertThat(response.getPlace()).isEqualTo(event.getPlace());
            assertThat(response.getEventType()).isEqualTo(event.getEventType());
            assertThat(response.getDateTime()).isEqualTo(event.getDateTime());
        }
    }

    @Test
    void deleteEventTest() throws IOException {
        // Arrange
        var eventId = 1L;

        // Act
        rabbitMQEventMessaging.deleteEvent(eventId);

        // Assert
        try (Connection connection = connectionFactory.createConnection()) {
            var channel = connection.createChannel(false);
            var getResponse = channel.basicGet(DELETE_EVENT_QUEUE, true);

            long response = (Long) longMessageConverter.fromMessage(new Message(getResponse.getBody()));

            assertThat(response).isEqualTo(eventId);
        }
    }

    private ObjectMapper getObjectMapper() {
        var objectMapper = Jackson2ObjectMapperBuilder.json()
                .createXmlMapper(false)
                .build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return objectMapper;
    }
}
