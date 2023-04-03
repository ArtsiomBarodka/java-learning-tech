package com.epam.app.service;

import com.epam.app.model.OrderStatus;
import com.epam.app.model.dto.Order;
import org.springframework.lang.NonNull;

public interface OrderService {
    @NonNull Order createOrder(@NonNull Order order);

    @NonNull Order updateOrderStatus (@NonNull Long orderId, @NonNull OrderStatus orderStatus);

    @NonNull Order getOrderById (@NonNull Long orderId);
}
