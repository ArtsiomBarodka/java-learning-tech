package com.epam.app.impl;

import com.epam.app.Event;
import com.epam.app.EventType;
import com.epam.app.config.kafka.KafkaConsumerConfig;
import com.epam.app.config.kafka.KafkaProducerConfig;
import com.epam.app.config.kafka.KafkaPropertiesConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("kafka")
@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {KafkaEventMessaging.class, KafkaConsumerConfig.class, KafkaProducerConfig.class, KafkaPropertiesConfig.class})
public class KafkaEventMessagingTest {
    private static final String CREATE_EVENT_TOPIC = "createEvent-testTopic";
    private static final String UPDATE_EVENT_TOPIC = "updateEvent-testTopic";
    private static final String DELETE_EVENT_TOPIC = "deleteEvent-testTopic";

    @Container
    private static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.10"));

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.bootstrapAddress", KAFKA::getBootstrapServers);
        registry.add("kafka.topic.create-event.name", () -> CREATE_EVENT_TOPIC);
        registry.add("kafka.topic.update-event.name", () -> UPDATE_EVENT_TOPIC);
        registry.add("kafka.topic.delete-event.name", () -> DELETE_EVENT_TOPIC);
    }

    @Autowired
    private KafkaEventMessaging kafkaEventMessaging;

    @Test
    void createEventTest() {
        // Arrange
        var event = new Event(1L, "title", "place", "speaker", EventType.WORKSHOP, LocalDateTime.now());

        // Act
        kafkaEventMessaging.createEvent(event);

        // Assert
        try (Consumer<String, Event> consumer = createEventConsumer(CREATE_EVENT_TOPIC)) {
            ConsumerRecords<String, Event> records = KafkaTestUtils.getRecords(consumer);

            assertThat(records.count()).isEqualTo(1);

            ConsumerRecord<String, Event> record = records.iterator().next();
            assertThat(record.key()).isEqualTo(String.valueOf(event.getEventId()));
            assertThat(record.value().getTitle()).isEqualTo(event.getTitle());
            assertThat(record.value().getSpeaker()).isEqualTo(event.getSpeaker());
            assertThat(record.value().getPlace()).isEqualTo(event.getPlace());
            assertThat(record.value().getEventType()).isEqualTo(event.getEventType());
            assertThat(record.value().getDateTime()).isEqualTo(event.getDateTime());
        }
    }

    @Test
    void updateEventTest() {
        // Arrange
        var event = new Event(1L, "title", "place", "speaker", EventType.WORKSHOP, LocalDateTime.now());

        // Act
        kafkaEventMessaging.updateEvent(event);

        // Assert
        try (Consumer<String, Event> consumer = createEventConsumer(UPDATE_EVENT_TOPIC)) {
            ConsumerRecords<String, Event> records = KafkaTestUtils.getRecords(consumer);

            assertThat(records.count()).isEqualTo(1);

            ConsumerRecord<String, Event> record = records.iterator().next();
            assertThat(record.key()).isEqualTo(String.valueOf(event.getEventId()));
            assertThat(record.value().getTitle()).isEqualTo(event.getTitle());
            assertThat(record.value().getSpeaker()).isEqualTo(event.getSpeaker());
            assertThat(record.value().getPlace()).isEqualTo(event.getPlace());
            assertThat(record.value().getEventType()).isEqualTo(event.getEventType());
            assertThat(record.value().getDateTime()).isEqualTo(event.getDateTime());
        }
    }

    @Test
    void deleteEventTest() {
        // Arrange
        var eventId = 1L;

        // Act
        kafkaEventMessaging.deleteEvent(eventId);

        // Assert
        try (Consumer<String, Long> consumer = createDeleteEventConsumer(DELETE_EVENT_TOPIC)) {
            ConsumerRecords<String, Long> records = KafkaTestUtils.getRecords(consumer);

            assertThat(records.count()).isEqualTo(1);

            ConsumerRecord<String, Long> record = records.iterator().next();
            assertThat(record.key()).isEqualTo(String.valueOf(eventId));
            assertThat(record.value().longValue()).isEqualTo(eventId);
        }
    }

    private Consumer<String, Event> createEventConsumer(String topic) {
        Consumer<String, Event> consumer = new DefaultKafkaConsumerFactory<>(
                getConsumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(Event.class))
                .createConsumer();

        consumer.subscribe(Collections.singletonList(topic));

        return consumer;
    }

    private Consumer<String, Long> createDeleteEventConsumer(String topic) {
        Consumer<String, Long> consumer = new DefaultKafkaConsumerFactory<>(
                getConsumerProps(),
                new StringDeserializer(),
                new LongDeserializer())
                .createConsumer();

        consumer.subscribe(Collections.singletonList(topic));

        return consumer;
    }

    private Map<String, Object> getConsumerProps() {
        return KafkaTestUtils.consumerProps(KAFKA.getBootstrapServers(), "test-group", "false");
    }
}
