package com.epam.app.producer;

import com.epam.app.model.DeveloperMessage;
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
    private final KafkaTemplate<String, DeveloperMessage> kafkaTemplate;
    @Value(value = "${kafka.topic.task4.name}")
    private String kafkaTopicTask4Name;

    public void produceMessage(@NonNull DeveloperMessage message) {
        kafkaTemplate.send(kafkaTopicTask4Name, message).whenComplete(((result, ex) -> {
            if (ex == null) {
                log.info("Message (= {}) sent to topic: {}", message, kafkaTopicTask4Name);
            }
        }));
    }
}
