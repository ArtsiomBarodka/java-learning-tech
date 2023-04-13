package com.epam.app.facade;

import com.epam.app.model.OrderStatus;
import com.epam.app.model.dto.Order;
import com.epam.app.model.request.CreateOrderRequest;
import org.springframework.lang.NonNull;

public interface OrderFacade {
    @NonNull
    Order createOrder(@NonNull CreateOrderRequest createOrderRequest);

    @NonNull Order updateOrderStatus (@NonNull Long orderId, @NonNull OrderStatus orderStatus);

    @NonNull Order getOrderById (@NonNull Long orderId);
}
