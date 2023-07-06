package com.epam.app.processor;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Log4j2
@Component
public class MessagesProcessor {
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<Long> LONG_SERDE = Serdes.Long();
    private static final String MESSAGE_DELIMITER = ":";
    private static final String VALUE_JOINING_SEPARATOR = "#####";
    private static final Predicate<String, String> VALIDATION_PREDICATE = (key, value) -> value != null && value.contains(MESSAGE_DELIMITER);

    @Value(value = "${kafka.topic.task3-1.name}")
    private String kafkaTopicOneName;
    @Value(value = "${kafka.topic.task3-2.name}")
    private String kafkaTopicTwoName;

    @Autowired
    public void processPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> streamOne = streamsBuilder.stream(kafkaTopicOneName, Consumed.with(STRING_SERDE, STRING_SERDE));
        KStream<String, String> streamTwo = streamsBuilder.stream(kafkaTopicTwoName, Consumed.with(STRING_SERDE, STRING_SERDE));

        KStream<Long, String> preparedStreamOne = streamOne
                .filter(VALIDATION_PREDICATE)
                .map(((key, value) -> {
                    var parts = value.split(MESSAGE_DELIMITER);
                    return KeyValue.pair(Long.parseLong(parts[0]), parts[1]);
                }))
                .peek(((key, value) -> log.info("After created new key: Topic = {}, key = {}, value = {}", kafkaTopicOneName, key, value)));

        KStream<Long, String> preparedStreamTwo = streamTwo
                .filter(VALIDATION_PREDICATE)
                .map(((key, value) -> {
                    var parts = value.split(MESSAGE_DELIMITER);
                    return KeyValue.pair(Long.parseLong(parts[0]), parts[1]);
                }))
                .peek(((key, value) -> log.info("After created new key: Topic = {}, key = {}, value = {}", kafkaTopicTwoName, key, value)));

        preparedStreamOne.join(
                        preparedStreamTwo,
                        (leftValue, rightValue) -> leftValue + VALUE_JOINING_SEPARATOR + rightValue,
                        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(30)),
                        StreamJoined.with(LONG_SERDE, STRING_SERDE, STRING_SERDE))
                .foreach(((key, value) -> log.info("After joining: key = {}, value = {}", key, value)));
    }
}
