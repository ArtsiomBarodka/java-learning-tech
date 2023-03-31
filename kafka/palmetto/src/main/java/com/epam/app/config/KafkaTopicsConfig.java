package com.epam.app.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {
    private final PropertiesConfig propertiesConfig;

    @Bean
    public NewTopic createOrderTopic() {
        return TopicBuilder.name(propertiesConfig.getKafkaTopicOrderName())
                .partitions(propertiesConfig.getKafkaTopicOrderPartitions())
                .replicas(propertiesConfig.getKafkaTopicOrderReplicas())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }

    @Bean
    public NewTopic createNotificationTopic() {
        return TopicBuilder.name(propertiesConfig.getKafkaTopicNotificationName())
                .partitions(propertiesConfig.getKafkaTopicNotificationPartitions())
                .replicas(propertiesConfig.getKafkaTopicNotificationReplicas())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }
}
