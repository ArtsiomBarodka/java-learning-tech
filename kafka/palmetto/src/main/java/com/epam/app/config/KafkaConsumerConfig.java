package com.epam.app.config;

import com.epam.app.exception.ValidationException;
import com.epam.app.model.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final PropertiesConfig propertiesConfig;
    private final KafkaTemplate<String, OrderMessage> orderDltKafkaTemplate;

    private ConsumerFactory<String, OrderMessage> consumerOrderFactory() {
        JsonDeserializer<OrderMessage> deserializer = new JsonDeserializer<>(OrderMessage.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.getKafkaServer());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertiesConfig.getKafkaConsumerOrderOffset());

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    private DefaultErrorHandler retryErrorHandler() {
        BackOff fixedBackOff = new FixedBackOff(TimeUnit.SECONDS.toMillis(propertiesConfig.getKafkaConsumerOrderRetryInterval()), propertiesConfig.getKafkaConsumerOrderRetryAttempts());
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, e) -> {
            var message = (OrderMessage) consumerRecord.value();
            log.error("Retry attempts are finished for message: {}", message);
        }, fixedBackOff);
        errorHandler.addRetryableExceptions(ValidationException.class);
        errorHandler.addNotRetryableExceptions(NullPointerException.class);
        errorHandler.setCommitRecovered(true);
        return errorHandler;
    }

    private DefaultErrorHandler retryErrorWithDlqHandler() {
        BackOff fixedBackOff = new FixedBackOff(TimeUnit.SECONDS.toMillis(propertiesConfig.getKafkaConsumerOrderRetryInterval()), propertiesConfig.getKafkaConsumerOrderRetryAttempts());
        var recoverer = new DeadLetterPublishingRecoverer(orderDltKafkaTemplate,
                (consumerRecord, e) -> {
                    var message = (OrderMessage) consumerRecord.value();
                    log.error("Retry attempts are finished for message: {}", message);
                    log.error("The message {} goes to the Dead Letter Queue", message);
                    return new TopicPartition(propertiesConfig.getKafkaTopicOrderDltName(), consumerRecord.partition());
                });
        var errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);
        errorHandler.addRetryableExceptions(ValidationException.class);
        errorHandler.addNotRetryableExceptions(NullPointerException.class);
        errorHandler.setCommitRecovered(true);
        return errorHandler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderMessage> orderRetryKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(propertiesConfig.getKafkaConsumerOrderCount());
        factory.setConsumerFactory(consumerOrderFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(retryErrorHandler());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderMessage> orderRetryWithDlqKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(propertiesConfig.getKafkaConsumerOrderCount());
        factory.setConsumerFactory(consumerOrderFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(retryErrorWithDlqHandler());
        return factory;
    }
}
