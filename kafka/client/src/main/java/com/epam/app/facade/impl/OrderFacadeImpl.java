package com.epam.app.facade.impl;

import com.epam.app.facade.OrderFacade;
import com.epam.app.model.OrderStatus;
import com.epam.app.model.dto.Order;
import com.epam.app.model.dto.OrderItem;
import com.epam.app.model.request.CreateOrderRequest;
import com.epam.app.service.NotificationService;
import com.epam.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {
    private final OrderService orderService;
    private final NotificationService notificationService;

    @Override
    @NonNull
    public Order createOrder(@NonNull CreateOrderRequest createOrderRequest) {
        var createdOrder = orderService.createOrder(toOrder(createOrderRequest));
        notificationService.sendNotification(createdOrder);
        return createdOrder;
    }

    @Override
    @NonNull
    public Order updateOrderStatus(@NonNull Long orderId, @NonNull OrderStatus orderStatus) {
        return orderService.updateOrderStatus(orderId, orderStatus);
    }

    @Override
    @NonNull
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Order getOrderById(@NonNull Long orderId) {
        return orderService.getOrderById(orderId);
    }

    private Order toOrder(CreateOrderRequest createOrderRequest) {
        var order = new Order();
        order.setUserId(createOrderRequest.getUserId());
        order.setOrderItems(createOrderRequest.getOrderItems()
                .stream()
                .map(item -> new OrderItem(item.getPizzaId(), item.getCount()))
                .toList());
        return order;
    }
}
