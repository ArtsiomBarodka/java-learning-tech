package com.epam.app.service;

import com.epam.app.model.Order;
import com.epam.app.model.OrderStatus;
import org.springframework.lang.NonNull;

public interface NotificationService {
    void notifyClient(@NonNull Order order, @NonNull OrderStatus orderStatus);
}
