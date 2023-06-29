package com.epam.app.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class TopicTask2Producer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value(value = "${kafka.topic.task2.name}")
    private String kafkaTopicTask2Name;

    public void produceMessage(@NonNull String message) {
        kafkaTemplate.send(kafkaTopicTask2Name, message).whenComplete(((result, ex) -> {
            if (ex == null) {
                log.info("Message (= {}) sent to topic: {}", message, kafkaTopicTask2Name);
            }
        }));
    }
}
