package com.epam.app.config;

import com.epam.app.model.NotificationMessage;
import com.epam.app.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final PropertiesConfig propertiesConfig;

    private ConsumerFactory<String, NotificationMessage> consumerNotificationFactory() {
        JsonDeserializer<NotificationMessage> deserializer = new JsonDeserializer<>(NotificationMessage.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.getKafkaServer());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertiesConfig.getKafkaConsumerNotificationOffset());

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationMessage> notificationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(propertiesConfig.getKafkaConsumerNotificationCount());
        factory.setConsumerFactory(consumerNotificationFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(this::filterRecord);
        return factory;
    }

    private boolean filterRecord(ConsumerRecord<String, NotificationMessage> record) {
        var value = record.value();
        return OrderStatus.READY_TO_DELIVER != value.getOrderStatus();
    }
}
