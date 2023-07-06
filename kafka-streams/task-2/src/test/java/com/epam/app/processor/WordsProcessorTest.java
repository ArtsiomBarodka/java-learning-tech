package com.epam.app.processor;

import nl.altindag.log.LogCaptor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
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
public class WordsProcessorTest {
    private static final String INPUT_TOPIC_1 = "input-topic-1";
    private static final String INPUT_TOPIC_2 = "input-topic-2";
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<Integer> INTEGER_SERDE = Serdes.Integer();

    @InjectMocks
    private WordsProcessor wordsProcessor;

    private LogCaptor logCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(wordsProcessor, "kafkaTopicShortWordsName", INPUT_TOPIC_1);
        ReflectionTestUtils.setField(wordsProcessor, "kafkaTopicLongWordsName", INPUT_TOPIC_2);
        logCaptor = LogCaptor.forClass(WordsProcessor.class);
        logCaptor.setLogLevelToInfo();
    }

    @Test
    void testProcessPipeline() {
        var keyOne = 1;
        var messageOne = "America";
        var keyTwo = 2;
        var messageTwo = "Accurate";

        var streamsBuilder = new StreamsBuilder();
        wordsProcessor.processPipeline(streamsBuilder);
        var topology = streamsBuilder.build();

        try (var topologyTestDriver = new TopologyTestDriver(topology, new Properties())) {

            var inputTopicOne = topologyTestDriver
                    .createInputTopic(INPUT_TOPIC_1, INTEGER_SERDE.serializer(), STRING_SERDE.serializer());

            var inputTopicTwo = topologyTestDriver
                    .createInputTopic(INPUT_TOPIC_2, INTEGER_SERDE.serializer(), STRING_SERDE.serializer());


            inputTopicOne.pipeInput(keyOne, messageOne);
            inputTopicTwo.pipeInput(keyTwo, messageTwo);
        }

        var infoLogs = logCaptor.getInfoLogs();
        assertThat(infoLogs)
                .isNotEmpty()
                .hasSize(2)
                .contains("After merging streams: key = %d, value = %s".formatted(keyOne, messageOne))
                .contains("After merging streams: key = %d, value = %s".formatted(keyTwo, messageTwo));
    }

    @Test
    void testProcessPipeline_filteredMessage() {
        var keyOne = 1;
        var messageOne = "America";
        var keyTwo = 2;
        var messageTwo = "Song";

        var streamsBuilder = new StreamsBuilder();
        wordsProcessor.processPipeline(streamsBuilder);
        var topology = streamsBuilder.build();

        try (var topologyTestDriver = new TopologyTestDriver(topology, new Properties())) {

            var inputTopicOne = topologyTestDriver
                    .createInputTopic(INPUT_TOPIC_1, INTEGER_SERDE.serializer(), STRING_SERDE.serializer());

            var inputTopicTwo = topologyTestDriver
                    .createInputTopic(INPUT_TOPIC_2, INTEGER_SERDE.serializer(), STRING_SERDE.serializer());


            inputTopicOne.pipeInput(keyOne, messageOne);
            inputTopicTwo.pipeInput(keyTwo, messageTwo);
        }

        var infoLogs = logCaptor.getInfoLogs();
        assertThat(infoLogs)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly("After merging streams: key = %d, value = %s".formatted(keyOne, messageOne));
    }
}
