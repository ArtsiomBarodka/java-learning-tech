package com.epam.app.service.impl;

import com.epam.app.config.PropertiesConfig;
import com.epam.app.model.NotificationMessage;
import com.epam.app.model.Order;
import com.epam.app.model.OrderStatus;
import com.epam.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final KafkaTemplate<String, NotificationMessage> notificationKafkaTemplate;
    private final PropertiesConfig propertiesConfig;

    @Override
    public void notifyClient(@NonNull Order order, @NonNull OrderStatus orderStatus) {
        String topic = propertiesConfig.getKafkaTopicNotificationName();
        String key = String.valueOf(order.getUserId());
        var message = new NotificationMessage(order.getOrderId(), order.getUserId(), orderStatus);
        log.info("Sending Order notification to {} topic with id {} and Message: {}", topic, key, message);
        notificationKafkaTemplate.send(topic, key, message);
    }

}
