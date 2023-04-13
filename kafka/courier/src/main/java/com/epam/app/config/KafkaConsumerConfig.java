package com.epam.app.config;

import com.epam.app.exception.ValidationException;
import com.epam.app.model.NotificationMessage;
import com.epam.app.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableKafkaRetryTopic
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
    public TaskScheduler scheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    public RetryTopicConfiguration myRetryTopic(KafkaTemplate<String, NotificationMessage> kafkaTemplate) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .listenerFactory(notificationKafkaListenerContainerFactory())
                .autoCreateTopicsWith(propertiesConfig.getKafkaTopicNotificationRetryAndDlqPartitions(), propertiesConfig.getKafkaTopicNotificationRetryAndDlqReplicas())
                .fixedBackOff(TimeUnit.SECONDS.toMillis(propertiesConfig.getKafkaConsumerNotificationRetryInterval()))
                .maxAttempts(propertiesConfig.getKafkaConsumerNotificationRetryAttempts())
                .includeTopic(propertiesConfig.getKafkaTopicNotificationName())
                .retryOn(ValidationException.class)
                .create(kafkaTemplate);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configs = Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.getKafkaServer());
        return new KafkaAdmin(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationMessage> notificationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(propertiesConfig.getKafkaConsumerNotificationCount());
        factory.setConsumerFactory(consumerNotificationFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(this::filterRecord);
        return factory;
    }

    private boolean filterRecord(ConsumerRecord<String, NotificationMessage> record) {
        var value = record.value();
        return OrderStatus.READY_TO_DELIVER != value.getOrderStatus();
    }
}
