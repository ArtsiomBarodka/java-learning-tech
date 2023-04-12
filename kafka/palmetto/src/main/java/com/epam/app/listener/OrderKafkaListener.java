package com.epam.app.listener;

import com.epam.app.facade.CookingFacade;
import com.epam.app.model.OrderMessage;
import com.epam.app.utils.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaListener {
    private final CookingFacade cookingFacade;
    private final Validator validator;

    @KafkaListener(topics = "#{'${kafka.topic.order.name}'.split(',')}",
            groupId = "${kafka.consumer.order.palmetto.group.id}",
            containerFactory = "orderRetryWithDlqKafkaListenerContainerFactory")
    public void processOrder(OrderMessage orderMessage,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_KEY) String key,
                             Acknowledgment acknowledgment) {
        log.info("New Order is receive: topic = {}, key = {}, order = {}", topic, key, orderMessage);
        validator.validateMessage(orderMessage);
        cookingFacade.cook(orderMessage);
        acknowledgment.acknowledge();
    }
}
