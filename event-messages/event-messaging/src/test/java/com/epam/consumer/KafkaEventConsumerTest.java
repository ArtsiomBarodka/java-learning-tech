package com.epam.consumer;

import com.epam.app.Event;
import com.epam.app.EventService;
import com.epam.app.EventType;
import com.epam.app.config.kafka.KafkaConsumerConfig;
import com.epam.app.config.kafka.KafkaPropertiesConfig;
import com.epam.app.consumer.KafkaEventConsumer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("kafka")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {KafkaEventConsumerTest.TestKafkaConfig.class, KafkaEventConsumer.class, KafkaConsumerConfig.class, KafkaPropertiesConfig.class, EventService.class})
public class KafkaEventConsumerTest {
    private static final String CREATE_REQUEST_EVENT_TOPIC = "createRequestEvent-testTopic";
    private static final String UPDATE_REQUEST_EVENT_TOPIC = "updateRequestEvent-testTopic";
    private static final String DELETE_REQUEST_EVENT_TOPIC = "deleteRequestEvent-testTopic";

    @Container
    private static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.10"));

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.bootstrapAddress", KAFKA::getBootstrapServers);
        registry.add("kafka.topic.create-request.name", () -> CREATE_REQUEST_EVENT_TOPIC);
        registry.add("kafka.topic.update-request.name", () -> UPDATE_REQUEST_EVENT_TOPIC);
        registry.add("kafka.topic.delete-request.name", () -> DELETE_REQUEST_EVENT_TOPIC);
        registry.add("kafka.consumer.request.offset", () -> "earliest");
    }

    @Autowired
    private KafkaTemplate<String, Event> kafkaEventTestTemplate;

    @Autowired
    private KafkaTemplate<String, Long> kafkaDeleteEventTestTemplate;

    @MockBean
    private EventService eventService;

    @Test
    void createEventRequestTest() {
        // Arrange
        var key = "key";
        var event = new Event(1L, "title", "place", "speaker", EventType.WORKSHOP, LocalDateTime.now());

        // Act
        kafkaEventTestTemplate.send(CREATE_REQUEST_EVENT_TOPIC, key, event);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(eventService, times(1)).createEvent(eq(event));
        });
    }

    @Test
    void updateEventRequestTest() {
        // Arrange
        var key = "key";
        var event = new Event(1L, "title", "place", "speaker", EventType.WORKSHOP, LocalDateTime.now());

        // Act
        kafkaEventTestTemplate.send(UPDATE_REQUEST_EVENT_TOPIC, key, event);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(eventService, times(1)).updateEvent(eq(event.getEventId()), eq(event));
        });
    }

    @Test
    void deleteEventRequestTest() {
        // Arrange
        var key = "key";
        var eventId = 1L;

        // Act
        kafkaDeleteEventTestTemplate.send(DELETE_REQUEST_EVENT_TOPIC, key, eventId);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(eventService, times(1)).deleteEvent(eq(eventId));
        });
    }

    @TestConfiguration
    public static class TestKafkaConfig {
        @Bean
        public KafkaTemplate<String, Event> kafkaEventTestTemplate() {
            ProducerFactory<String, Event> producerFactory =
                    new DefaultKafkaProducerFactory<>(
                            getProducerProps(),
                            new StringSerializer(),
                            new JsonSerializer<>());
            return new KafkaTemplate<>(producerFactory);
        }

        @Bean
        public KafkaTemplate<String, Long> kafkaDeleteEventTestTemplate() {
            ProducerFactory<String, Long> producerFactory =
                    new DefaultKafkaProducerFactory<>(
                            getProducerProps(),
                            new StringSerializer(),
                            new LongSerializer());
            return new KafkaTemplate<>(producerFactory);
        }

        private Map<String, Object> getProducerProps() {
            return KafkaTestUtils.producerProps(KAFKA.getBootstrapServers());
        }
    }
}
