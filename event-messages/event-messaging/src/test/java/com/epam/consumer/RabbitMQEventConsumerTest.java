package com.epam.consumer;

import com.epam.app.Event;
import com.epam.app.EventService;
import com.epam.app.EventType;
import com.epam.app.config.rabbitmq.RabbitMQBasicConfig;
import com.epam.app.config.rabbitmq.RabbitMQConsumerConfig;
import com.epam.app.config.rabbitmq.RabbitMQPropertiesConfig;
import com.epam.app.config.rabbitmq.RabbitMQQueuesConfig;
import com.epam.app.consumer.RabbitMQEventConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("rabbitmq")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {RabbitMQEventConsumerTest.TestRabbitMQConfig.class, RabbitMQQueuesConfig.class,
        RabbitMQBasicConfig.class, RabbitMQEventConsumer.class, RabbitMQConsumerConfig.class,
        RabbitMQPropertiesConfig.class, EventService.class})
public class RabbitMQEventConsumerTest {
    private static final String CREATE_REQUEST_EVENT_QUEUE = "createRequestEvent-testQueue";
    private static final String UPDATE_REQUEST_EVENT_QUEUE = "updateRequestEvent-testQueue";
    private static final String DELETE_REQUEST_EVENT_QUEUE = "deleteRequestEvent-testQueue";

    @Container
    private static final RabbitMQContainer RABBIT_MQ =
            new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.11.13-management-alpine"));

    @DynamicPropertySource
    static void registerRabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("rabbit.host", RABBIT_MQ::getHost);
        registry.add("rabbit.port", RABBIT_MQ::getAmqpPort);
        registry.add("rabbit.username", RABBIT_MQ::getAdminUsername);
        registry.add("rabbit.password", RABBIT_MQ::getAdminPassword);
        registry.add("rabbit.queue.create-request.name", () -> CREATE_REQUEST_EVENT_QUEUE);
        registry.add("rabbit.queue.update-request.name", () -> UPDATE_REQUEST_EVENT_QUEUE);
        registry.add("rabbit.queue.delete-request.name", () -> DELETE_REQUEST_EVENT_QUEUE);
    }

    @Autowired
    private RabbitTemplate eventRabbitTestTemplate;

    @Autowired
    private RabbitTemplate deleteRabbitTestTemplate;

    @MockBean
    private EventService eventService;

    @Test
    void createEventRequestTest() {
        // Arrange
        var event = new Event(1L, "title", "place", "speaker", EventType.WORKSHOP, LocalDateTime.now());

        // Act
        eventRabbitTestTemplate.convertAndSend(CREATE_REQUEST_EVENT_QUEUE, event);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(eventService, times(1)).createEvent(eq(event));
        });
    }

    @Test
    void updateEventRequestTest() {
        // Arrange
        var event = new Event(1L, "title", "place", "speaker", EventType.WORKSHOP, LocalDateTime.now());

        // Act
        eventRabbitTestTemplate.convertAndSend(UPDATE_REQUEST_EVENT_QUEUE, event);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(eventService, times(1)).updateEvent(eq(event.getEventId()), eq(event));
        });
    }

    @Test
    void deleteEventRequestTest() {
        // Arrange
        var eventId = 1L;

        // Act
        deleteRabbitTestTemplate.convertAndSend(DELETE_REQUEST_EVENT_QUEUE, eventId);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(eventService, times(1)).deleteEvent(eq(eventId));
        });
    }

    @TestConfiguration
    public static class TestRabbitMQConfig {
        @Autowired
        private ConnectionFactory connectionFactory;
        @Autowired
        private MessageConverter longMessageConverter;
        @Autowired
        private MessageConverter jsonMessageConverter;

        @Bean
        public RabbitTemplate eventRabbitTestTemplate() {
            var template = new RabbitTemplate(connectionFactory);
            template.setMessageConverter(jsonMessageConverter);
            return template;
        }

        @Bean
        public RabbitTemplate deleteRabbitTestTemplate() {
            var template = new RabbitTemplate(connectionFactory);
            template.setMessageConverter(longMessageConverter);
            return template;
        }
    }
}
