package com.epam.app.processor;

import com.epam.app.model.DeveloperMessage;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DeveloperProcessor {
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<DeveloperMessage> DEVELOPER_SERDE = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(DeveloperMessage.class));

    @Value(value = "${kafka.topic.task4.name}")
    private String kafkaTopicTask4Name;

    @Autowired
    public void processPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, DeveloperMessage> stream = streamsBuilder.stream(kafkaTopicTask4Name, Consumed.with(STRING_SERDE, DEVELOPER_SERDE));

        stream
                .filter(((key, value) -> value != null))
                .foreach((key, value) -> log.info("Stream: Topic = {}, key = {}, value = {}", kafkaTopicTask4Name, key, value));
    }
}
