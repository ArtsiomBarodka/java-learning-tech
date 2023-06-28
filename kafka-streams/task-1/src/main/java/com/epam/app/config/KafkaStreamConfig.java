package com.epam.app.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;

@EnableKafkaStreams
@EnableKafka
@Configuration
public class KafkaStreamConfig {
    @Value(value = "${kafka.bootstrapAddress}")
    private String kafkaBootstrapAddress;
    @Value(value = "${kafka.stream.application.id}")
    private String kafkaStreamAppId;

    @Value(value = "${kafka.topic.task1-1.name}")
    private String kafkaTopicTask1OneName;
    @Value(value = "${kafka.topic.task1-2.name}")
    private String kafkaTopicTask1TwoName;


    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, kafkaStreamAppId);
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress);

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configs = Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createOneTask1Topic() {
        return TopicBuilder
                .name(kafkaTopicTask1OneName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic createTwoTask1Topic() {
        return TopicBuilder
                .name(kafkaTopicTask1TwoName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
