package com.epam.app.listener;

import com.epam.app.config.KafkaConsumerConfig;
import com.epam.app.config.PropertiesConfig;
import com.epam.app.facade.OrderFacade;
import com.epam.app.model.NotificationMessage;
import com.epam.app.model.OrderStatus;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Testcontainers
@ContextConfiguration(classes = {NotificationKafkaListenerTest.TestConfig.class, PropertiesConfig.class, KafkaConsumerConfig.class, NotificationKafkaListener.class})
public class NotificationKafkaListenerTest {
    private static final String TOPIC = "Test-topic";

    @Container
    private static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.10"));

    @Autowired
    private KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    @MockBean
    private OrderFacade orderFacade;

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.bootstrapAddress", KAFKA::getBootstrapServers);
        registry.add("kafka.topic.notification.name", () -> TOPIC);
        registry.add("kafka.consumer.notification.offset", () -> "earliest");
    }

    @Test
    void processOrderTest() {
        // Arrange
        var key = "key";
        var notificationMessage = new NotificationMessage(1L, 1L, OrderStatus.DELIVERING);

        // Act
        kafkaTemplate.send(TOPIC, key, notificationMessage);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(orderFacade, times(1))
                    .updateOrderStatus(eq(notificationMessage.getOrderId()), eq(notificationMessage.getOrderStatus()));
        });
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public KafkaTemplate<String, NotificationMessage> kafkaTemplate() {
            Map<String, Object> producerProps = KafkaTestUtils.producerProps(KAFKA.getBootstrapServers());
            ProducerFactory<String, NotificationMessage> producerFactory =
                    new DefaultKafkaProducerFactory<>(
                            producerProps,
                            new StringSerializer(),
                            new JsonSerializer<>());
            return new KafkaTemplate<>(producerFactory);
        }
    }
}
