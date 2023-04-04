package com.epam.app.service.impl;

import com.epam.app.config.KafkaProducerConfig;
import com.epam.app.config.PropertiesConfig;
import com.epam.app.model.OrderItemMessage;
import com.epam.app.model.OrderMessage;
import com.epam.app.model.OrderStatus;
import com.epam.app.model.dto.Order;
import com.epam.app.model.dto.OrderItem;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {NotificationServiceImpl.class, PropertiesConfig.class, KafkaProducerConfig.class})
public class NotificationServiceImplTest {
    private static final String TOPIC = "Test-topic";

    @Container
    private static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.10"));

    @Autowired
    private NotificationServiceImpl notificationService;

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.bootstrapAddress", KAFKA::getBootstrapServers);
        registry.add("kafka.topic.order.name", () -> TOPIC);
    }

    @Test
    void sendNotificationTest() {
        // Arrange
        var orderItem = new OrderItem(1L, 1);
        var order = new Order(1L, 1L, LocalDateTime.now(), LocalDateTime.now(),
                List.of(orderItem), OrderStatus.CREATED);

        // Act
        notificationService.sendNotification(order);

        // Assert
        try (Consumer<String, OrderMessage> consumer = createConsumer()) {
            ConsumerRecords<String, OrderMessage> records = KafkaTestUtils.getRecords(consumer);

            assertThat(records.count()).isEqualTo(1);

            ConsumerRecord<String, OrderMessage> record = records.iterator().next();
            assertThat(record.key()).isEqualTo(String.valueOf(order.getUserId()));
            assertThat(record.value().getOrderId()).isEqualTo(order.getOrderId());
            assertThat(record.value().getUserId()).isEqualTo(order.getUserId());

            assertThat(record.value().getOrderItems().size()).isEqualTo(1);

            OrderItemMessage orderItemMessage = record.value().getOrderItems().get(0);
            assertThat(orderItemMessage.getPizzaId()).isEqualTo(orderItem.getPizzaId());
            assertThat(orderItemMessage.getCount()).isEqualTo(orderItem.getCount());
        }
    }

    private Consumer<String, OrderMessage> createConsumer() {
        var consumerProps = KafkaTestUtils.consumerProps(KAFKA.getBootstrapServers(), "test-group", "false");
        Consumer<String, OrderMessage> consumer = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(OrderMessage.class))
                .createConsumer();

        consumer.subscribe(Collections.singletonList(TOPIC));

        return consumer;
    }
}
