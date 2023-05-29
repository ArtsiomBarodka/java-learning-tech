package com.epam.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertiesConfig {
    @Value("${kafka.bootstrapAddress}")
    private String kafkaServer;

    @Value("${kafka.consumer.avro.offset}")
    private String kafkaConsumerAvroOffset;

    @Value("${kafka.consumer.avro.count}")
    private int kafkaConsumerAvroCount;
}
