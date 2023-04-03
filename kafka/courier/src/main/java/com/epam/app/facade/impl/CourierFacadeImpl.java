package com.epam.app.facade.impl;

import com.epam.app.facade.CourierFacade;
import com.epam.app.model.NotificationMessage;
import com.epam.app.model.Order;
import com.epam.app.model.OrderStatus;
import com.epam.app.service.CourierService;
import com.epam.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourierFacadeImpl implements CourierFacade {
    private final CourierService courierService;
    private final NotificationService notificationService;

    @Override
    public void deliver(NotificationMessage notificationMessage) {
        var order = new Order(notificationMessage.getOrderId(), notificationMessage.getUserId());
        notificationService.notifyClient(order, OrderStatus.DELIVERING);
        courierService.deliverOrder(order);
        notificationService.notifyClient(order, OrderStatus.DELIVERED);
    }
}
