package com.epam.app.processor;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class WordsProcessor {
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<Integer> INTEGER_SERDE = Serdes.Integer();
    private static final String WORD_MATCHING = "a";

    @Value(value = "${kafka.topic.short-words.name}")
    private String kafkaTopicShortWordsName;
    @Value(value = "${kafka.topic.long-words.name}")
    private String kafkaTopicLongWordsName;

    @Autowired
    public void processPipeline(StreamsBuilder streamsBuilder) {
        KStream<Integer, String> shortWordsStream = streamsBuilder
                .stream(kafkaTopicShortWordsName, Consumed.with(INTEGER_SERDE, STRING_SERDE))
                .filter(((key, value) -> value.contains(WORD_MATCHING)));

        KStream<Integer, String> longWordsStream = streamsBuilder
                .stream(kafkaTopicLongWordsName, Consumed.with(INTEGER_SERDE, STRING_SERDE))
                .filter(((key, value) -> value.contains(WORD_MATCHING)));

        shortWordsStream
                .merge(longWordsStream)
                .foreach((key, value) -> log.info("After merging streams: key = {}, value = {}", key, value));
    }
}
