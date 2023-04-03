package com.epam.app.service.impl;

import com.epam.app.config.PropertiesConfig;
import com.epam.app.model.OrderItemMessage;
import com.epam.app.model.OrderMessage;
import com.epam.app.model.dto.Order;
import com.epam.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;
    private final PropertiesConfig propertiesConfig;

    @Override
    public void sendNotification(Order order) {
        String topic = propertiesConfig.getKafkaTopicOrderName();
        String key = String.valueOf(order.getUserId());
        OrderMessage message = toOrderMessage(order);
        log.info("Sending Order notification to {} topic with id {} and Message: {}", topic, key, message);
        kafkaTemplate.send(topic, key, message);
    }

    private OrderMessage toOrderMessage(Order order) {
        var orderMessage = new OrderMessage();
        orderMessage.setOrderId(order.getOrderId());
        orderMessage.setUserId(order.getUserId());
        orderMessage.setOrderItems(order.getOrderItems()
                .stream()
                .map(item -> new OrderItemMessage(item.getPizzaId(), item.getCount()))
                .toList());

        return orderMessage;
    }
}
