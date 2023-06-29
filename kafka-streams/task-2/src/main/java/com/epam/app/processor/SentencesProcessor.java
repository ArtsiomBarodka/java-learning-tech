package com.epam.app.processor;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Log4j2
@Component
public class SentencesProcessor {
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final String DIVIDE_SENTENCES_REGEXP = "\\s+";
    private static final Predicate<Integer, String> SHORT_WORD_PREDICATE = (key, value) -> value.length() < 10;
    private static final Predicate<Integer, String> LONG_WORD_PREDICATE = (key, value) -> !SHORT_WORD_PREDICATE.test(key, value);

    @Value(value = "${kafka.topic.task2.name}")
    private String kafkaTopicTask2Name;
    @Value(value = "${kafka.topic.short-words.name}")
    private String kafkaTopicShortWordsName;
    @Value(value = "${kafka.topic.long-words.name}")
    private String kafkaTopicLongWordsName;

    @Autowired
    public void processPipeline(StreamsBuilder streamsBuilder) {
        KStream<Integer, String> wordsStream = streamsBuilder
                .stream(kafkaTopicTask2Name, Consumed.with(STRING_SERDE, STRING_SERDE))
                .filter(((key, value) -> value != null))
                .flatMapValues(value -> Arrays.asList(value.split(DIVIDE_SENTENCES_REGEXP)))
                .selectKey(((key, value) -> value.length()))
                .peek(((key, value) -> log.info("After splitting sentence: Topic = {}, key = {}, value = {}", kafkaTopicTask2Name, key, value)));

        wordsStream.split()
                .branch(SHORT_WORD_PREDICATE, Branched.withConsumer(ks -> ks
                        .peek((key, value) -> log.info("After splitting stream: Topic = {}, key = {}, value = {}", kafkaTopicShortWordsName, key, value))
                        .to(kafkaTopicShortWordsName)))
                .branch(LONG_WORD_PREDICATE, Branched.withConsumer(ks -> ks
                        .peek((key, value) -> log.info("After splitting stream: Topic = {}, key = {}, value = {}", kafkaTopicLongWordsName, key, value))
                        .to(kafkaTopicLongWordsName)));

    }

}
