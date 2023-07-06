package com.epam.app.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
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
    @Value(value = "${kafka.topic.task2.name}")
    private String kafkaTopicTask2Name;
    @Value(value = "${kafka.topic.short-words.name}")
    private String kafkaTopicShortWordsName;
    @Value(value = "${kafka.topic.long-words.name}")
    private String kafkaTopicLongWordsName;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, kafkaStreamAppId);
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configs = Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createTask2Topic() {
        return TopicBuilder
                .name(kafkaTopicTask2Name)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic createShortWordsTopic() {
        return TopicBuilder
                .name(kafkaTopicShortWordsName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic createLongWordsTopic() {
        return TopicBuilder
                .name(kafkaTopicLongWordsName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
