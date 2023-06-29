package com.epam.app.processor;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class SentencesProcessorTest {
    private static final String INPUT_TOPIC = "input-topic";
    private static final String OUTPUT_TOPIC_1 = "output-topic-1";
    private static final String OUTPUT_TOPIC_2 = "output-topic-2";
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<Integer> INTEGER_SERDE = Serdes.Integer();

    @InjectMocks
    private SentencesProcessor sentencesProcessor;

    private Properties streamProperties;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sentencesProcessor, "kafkaTopicTask2Name", INPUT_TOPIC);
        ReflectionTestUtils.setField(sentencesProcessor, "kafkaTopicShortWordsName", OUTPUT_TOPIC_1);
        ReflectionTestUtils.setField(sentencesProcessor, "kafkaTopicLongWordsName", OUTPUT_TOPIC_2);

        streamProperties = new Properties();
        streamProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, INTEGER_SERDE.getClass().getName());
        streamProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, STRING_SERDE.getClass().getName());
    }

    @Test
    void testProcessPipeline() {
        var shortPart = "short";
        var longPart = "thiIsTheLongPart";
        var message = "%s %s %s    %s".formatted(shortPart, longPart, shortPart, longPart);

        var streamsBuilder = new StreamsBuilder();
        sentencesProcessor.processPipeline(streamsBuilder);
        var topology = streamsBuilder.build();

        try (var topologyTestDriver = new TopologyTestDriver(topology, streamProperties)) {
            var inputTopic = topologyTestDriver
                    .createInputTopic(INPUT_TOPIC, STRING_SERDE.serializer(), STRING_SERDE.serializer());
            var outputTopicOne = topologyTestDriver
                    .createOutputTopic(OUTPUT_TOPIC_1, INTEGER_SERDE.deserializer(), STRING_SERDE.deserializer());
            var outputTopicTwo = topologyTestDriver
                    .createOutputTopic(OUTPUT_TOPIC_2, INTEGER_SERDE.deserializer(), STRING_SERDE.deserializer());

            inputTopic.pipeInput(null, message);

            assertThat(outputTopicOne.readKeyValuesToList())
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactly(
                            KeyValue.pair(shortPart.length(), shortPart),
                            KeyValue.pair(shortPart.length(), shortPart));

            assertThat(outputTopicTwo.readKeyValuesToList())
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactly(
                            KeyValue.pair(longPart.length(), longPart),
                            KeyValue.pair(longPart.length(), longPart));
        }
    }

    @Test
    void testProcessPipeline_filteredForNullMessages() {
        String message = null;
        var shortPart = "short";
        var longPart = "thiIsTheLongPart";
        var message2 = "%s %s %s    %s".formatted(shortPart, longPart, shortPart, longPart);

        var streamsBuilder = new StreamsBuilder();
        sentencesProcessor.processPipeline(streamsBuilder);
        var topology = streamsBuilder.build();

        try (var topologyTestDriver = new TopologyTestDriver(topology, streamProperties)) {
            var inputTopic = topologyTestDriver
                    .createInputTopic(INPUT_TOPIC, STRING_SERDE.serializer(), STRING_SERDE.serializer());
            var outputTopicOne = topologyTestDriver
                    .createOutputTopic(OUTPUT_TOPIC_1, INTEGER_SERDE.deserializer(), STRING_SERDE.deserializer());
            var outputTopicTwo = topologyTestDriver
                    .createOutputTopic(OUTPUT_TOPIC_2, INTEGER_SERDE.deserializer(), STRING_SERDE.deserializer());

            inputTopic.pipeInput(null, message);
            inputTopic.pipeInput(null, message2);

            assertThat(outputTopicOne.readKeyValuesToList())
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactly(
                            KeyValue.pair(shortPart.length(), shortPart),
                            KeyValue.pair(shortPart.length(), shortPart));

            assertThat(outputTopicTwo.readKeyValuesToList())
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactly(
                            KeyValue.pair(longPart.length(), longPart),
                            KeyValue.pair(longPart.length(), longPart));
        }
    }
}
