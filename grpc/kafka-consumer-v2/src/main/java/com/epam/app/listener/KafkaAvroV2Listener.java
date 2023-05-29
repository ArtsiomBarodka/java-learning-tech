package com.epam.app.listener;

import com.avro.User;
import com.epam.app.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAvroV2Listener {

    @KafkaListener(topics = "#{'${kafka.topic.avro.name}'.split(',')}",
            groupId = "${kafka.consumer.avro.kafka.group.id}",
            containerFactory = "avroRetryKafkaListenerContainerFactory")
    public void process(User user,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("New User is received: topic = {}, key = {}, user = {}", topic, key, user);
        if (user.getId() == 2L) {
            log.error("Users id can't be equal 2");
            throw new ValidationException("Users id can't be equal 2");
        }
    }
}
