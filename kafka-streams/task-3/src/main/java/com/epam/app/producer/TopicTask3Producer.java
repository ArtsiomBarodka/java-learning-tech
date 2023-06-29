package com.epam.app.producer;

import com.epam.app.model.MessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class TopicTask3Producer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value(value = "${kafka.topic.task3-1.name}")
    private String kafkaTopicOneName;
    @Value(value = "${kafka.topic.task3-2.name}")
    private String kafkaTopicTwoName;

    public void produceMessage(@NonNull MessageRequest messageRequest) {
        kafkaTemplate.send(kafkaTopicOneName, messageRequest.getTopicOneMessage()).whenComplete(((result, ex) -> {
            if (ex == null) {
                log.info("Message (= {}) sent to topic: {}", messageRequest.getTopicOneMessage(), kafkaTopicOneName);
            }
        }));

        kafkaTemplate.send(kafkaTopicTwoName, messageRequest.getTopicTwoMessage()).whenComplete(((result, ex) -> {
            if (ex == null) {
                log.info("Message (= {}) sent to topic: {}", messageRequest.getTopicTwoMessage(), kafkaTopicTwoName);
            }
        }));
    }
}
