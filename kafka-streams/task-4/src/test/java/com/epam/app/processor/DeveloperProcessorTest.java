package com.epam.app.processor;

import com.epam.app.model.DeveloperMessage;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DeveloperProcessorTest {
    private static final String INPUT_TOPIC = "input-topic";
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<DeveloperMessage> DEVELOPER_SERDE = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(DeveloperMessage.class));

    @InjectMocks
    private DeveloperProcessor developerProcessor;

    private LogCaptor logCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(developerProcessor, "kafkaTopicTask4Name", INPUT_TOPIC);
        logCaptor = LogCaptor.forClass(DeveloperProcessor.class);
        logCaptor.setLogLevelToInfo();
    }

    @Test
    void testProcessPipeline() {
        var developerMessage = new DeveloperMessage();
        developerMessage.setName("Name");
        developerMessage.setCompany("Company");
        developerMessage.setPosition("Position");
        developerMessage.setExperience(10);

        var streamsBuilder = new StreamsBuilder();
        developerProcessor.processPipeline(streamsBuilder);
        var topology = streamsBuilder.build();

        try (var topologyTestDriver = new TopologyTestDriver(topology, new Properties())) {

            var inputTopic = topologyTestDriver
                    .createInputTopic(INPUT_TOPIC, STRING_SERDE.serializer(), DEVELOPER_SERDE.serializer());


            inputTopic.pipeInput(null, developerMessage);
        }

        var infoLogs = logCaptor.getInfoLogs();
        assertThat(infoLogs)
                .isNotEmpty()
                .containsExactly("Stream: Topic = %s, key = null, value = DeveloperMessage(name=%s, company=%s, position=%s, experience=%d)"
                        .formatted(INPUT_TOPIC,
                                developerMessage.getName(),
                                developerMessage.getCompany(),
                                developerMessage.getPosition(),
                                developerMessage.getExperience()));
    }
}
