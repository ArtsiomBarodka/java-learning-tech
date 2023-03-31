package com.epam.app.service;

import com.epam.app.model.dto.Order;
import org.springframework.lang.NonNull;

public interface NotificationService {
    void sendNotification(@NonNull Order order);
}
