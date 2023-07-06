package com.epam.app.processor;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ReadWriteProcessor {
    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Value(value = "${kafka.topic.task1-1.name}")
    private String kafkaTopicTask1OneName;
    @Value(value = "${kafka.topic.task1-2.name}")
    private String kafkaTopicTask1TwoName;

    @Autowired
    public void processPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder
                .stream(kafkaTopicTask1OneName, Consumed.with(STRING_SERDE, STRING_SERDE));

        stream
                .peek(((key, value) -> log.info("Topic = {}, key = {}, value = {}", kafkaTopicTask1OneName, key, value)))
                .to(kafkaTopicTask1TwoName, Produced.with(STRING_SERDE, STRING_SERDE));
    }
}
