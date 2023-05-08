package com.epam.app.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

@Profile("kafka")
@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {
    private static final String COMPRESSION_TYPE = "zstd";

    private final KafkaPropertiesConfig kafkaPropertiesConfig;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configs = Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPropertiesConfig.getKafkaServer());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createCreateEventTopic() {
        return TopicBuilder.name(kafkaPropertiesConfig.getKafkaTopicCreateEventName())
                .partitions(kafkaPropertiesConfig.getKafkaTopicCreateEventPartitions())
                .replicas(kafkaPropertiesConfig.getKafkaTopicCreateEventReplicas())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE)
                .build();
    }

    @Bean
    public NewTopic createUpdateEventTopic() {
        return TopicBuilder.name(kafkaPropertiesConfig.getKafkaTopicUpdateEventName())
                .partitions(kafkaPropertiesConfig.getKafkaTopicUpdateEventPartitions())
                .replicas(kafkaPropertiesConfig.getKafkaTopicUpdateEventReplicas())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE)
                .build();
    }

    @Bean
    public NewTopic createDeleteEventTopic() {
        return TopicBuilder.name(kafkaPropertiesConfig.getKafkaTopicDeleteEventName())
                .partitions(kafkaPropertiesConfig.getKafkaTopicDeleteEventPartitions())
                .replicas(kafkaPropertiesConfig.getKafkaTopicDeleteEventReplicas())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE)
                .build();
    }

    @Bean
    public NewTopic createCreateRequestTopic() {
        return TopicBuilder.name(kafkaPropertiesConfig.getKafkaTopicCreateRequestName())
                .partitions(kafkaPropertiesConfig.getKafkaTopicCreateRequestPartitions())
                .replicas(kafkaPropertiesConfig.getKafkaTopicCreateRequestReplicas())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE)
                .build();
    }

    @Bean
    public NewTopic createUpdateRequestTopic() {
        return TopicBuilder.name(kafkaPropertiesConfig.getKafkaTopicUpdateRequestName())
                .partitions(kafkaPropertiesConfig.getKafkaTopicUpdateRequestPartitions())
                .replicas(kafkaPropertiesConfig.getKafkaTopicUpdateRequestReplicas())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE)
                .build();
    }

    @Bean
    public NewTopic createDeleteRequestTopic() {
        return TopicBuilder.name(kafkaPropertiesConfig.getKafkaTopicDeleteRequestName())
                .partitions(kafkaPropertiesConfig.getKafkaTopicDeleteRequestPartitions())
                .replicas(kafkaPropertiesConfig.getKafkaTopicDeleteRequestReplicas())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE)
                .build();
    }
}
