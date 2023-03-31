package com.epam.app.facade.impl;

import com.epam.app.facade.OrderFacade;
import com.epam.app.model.OrderStatus;
import com.epam.app.model.dto.Order;
import com.epam.app.model.request.CreateOrderRequest;
import com.epam.app.service.NotificationService;
import com.epam.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {
    private final OrderService orderService;
    private final NotificationService notificationService;

    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        var createdOrder = orderService.createOrder(createOrderRequest);
        notificationService.sendNotification(createdOrder);
        return createdOrder;
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        return orderService.updateOrderStatus(orderId, orderStatus);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderService.getOrderById(orderId);
    }
}
