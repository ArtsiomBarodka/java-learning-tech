package com.epam.app.listener;

import com.epam.app.facade.OrderFacade;
import com.epam.app.model.NotificationMessage;
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
public class NotificationKafkaListener {
    private final OrderFacade orderFacade;

    @KafkaListener(topics = "#{'${kafka.topic.notification.name}'.split(',')}",
            groupId = "${kafka.consumer.notification.client.group.id}",
            containerFactory = "notificationKafkaListenerContainerFactory")
    public void processOrder(NotificationMessage notificationMessage,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_KEY) String key,
                             Acknowledgment acknowledgment) {
        log.info("New Notification is received: topic = {}, key = {}, order = {}", topic, key, notificationMessage);
        orderFacade.updateOrderStatus(notificationMessage.getOrderId(), notificationMessage.getOrderStatus());
        acknowledgment.acknowledge();
    }
}
