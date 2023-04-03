package com.epam.app.facade.impl;

import com.epam.app.facade.CookingFacade;
import com.epam.app.model.Order;
import com.epam.app.model.OrderItem;
import com.epam.app.model.OrderMessage;
import com.epam.app.model.OrderStatus;
import com.epam.app.service.CookingService;
import com.epam.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookingFacadeImpl implements CookingFacade {
    private final CookingService cookingService;
    private final NotificationService notificationService;

    @Override
    public void cook(OrderMessage orderMessage) {
        var order = toOrder(orderMessage);
        notificationService.notifyClient(order, OrderStatus.COOKING);
        cookingService.cook(order);
        notificationService.notifyClient(order, OrderStatus.READY_TO_DELIVER);
    }

    private Order toOrder(OrderMessage orderMessage) {
        var order = new Order();
        order.setOrderId(orderMessage.getOrderId());
        order.setUserId(orderMessage.getUserId());
        order.setOrderItems(orderMessage.getOrderItems()
                .stream()
                .map(item -> new OrderItem(item.getPizzaId(), item.getCount()))
                .toList());

        return order;
    }
}
