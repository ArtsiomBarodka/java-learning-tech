package com.epam.app.listener;

import com.epam.app.config.KafkaConsumerConfig;
import com.epam.app.config.PropertiesConfig;
import com.epam.app.facade.CookingFacade;
import com.epam.app.model.OrderItemMessage;
import com.epam.app.model.OrderMessage;
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
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Testcontainers
@ContextConfiguration(classes = {OrderKafkaListenerTest.TestConfig.class, KafkaConsumerConfig.class, PropertiesConfig.class, OrderKafkaListener.class})
public class OrderKafkaListenerTest {
    private static final String TOPIC = "Test-topic";

    @Container
    private static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.10"));

    @Autowired
    private KafkaTemplate<String, OrderMessage> kafkaTemplate;

    @MockBean
    private CookingFacade cookingFacade;

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.bootstrapAddress", KAFKA::getBootstrapServers);
        registry.add("kafka.topic.order.name", () -> TOPIC);
        registry.add("kafka.consumer.order.offset", () -> "earliest");
    }

    @Test
    void processOrderTest() {
        // Arrange
        var key = "key";
        var orderMessage = new OrderMessage(1L, 1L, List.of(new OrderItemMessage(1L, 1)));

        // Act
        kafkaTemplate.send(TOPIC, key, orderMessage);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(cookingFacade, times(1)).cook(eq(orderMessage));
        });
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public KafkaTemplate<String, OrderMessage> kafkaTemplate() {
            Map<String, Object> producerProps = KafkaTestUtils.producerProps(KAFKA.getBootstrapServers());
            ProducerFactory<String, OrderMessage> producerFactory =
                    new DefaultKafkaProducerFactory<>(
                            producerProps,
                            new StringSerializer(),
                            new JsonSerializer<>());
            return new KafkaTemplate<>(producerFactory);
        }
    }
}
