package com.epam.app.consumer;

import com.epam.app.facade.OrderFacade;
import com.epam.app.model.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderStatusConsumer {
    private final OrderFacade orderFacade;

    @KafkaListener(topics = "#{'${kafka.topic.notification.name}'.split(',')}", containerFactory = "notificationKafkaListenerContainerFactory")
    public void processOrder(NotificationMessage notificationMessage,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.KEY) String msgKey) {
        log.info("Notification of Order process is receive: topic = {}, key = {}, orderId = {}", topic, msgKey, notificationMessage.getOrderId());

        orderFacade.updateOrderStatus(notificationMessage.getOrderId(), notificationMessage.getOrderStatus());
    }
}
