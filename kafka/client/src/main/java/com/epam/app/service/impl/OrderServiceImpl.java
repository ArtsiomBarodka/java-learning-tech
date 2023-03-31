package com.epam.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.model.OrderStatus;
import com.epam.app.model.dto.Order;
import com.epam.app.model.dto.OrderItem;
import com.epam.app.model.entity.OrderEntity;
import com.epam.app.model.entity.OrderItemEntity;
import com.epam.app.model.request.CreateOrderRequest;
import com.epam.app.repository.OrderRepository;
import com.epam.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        final var newOrderEntity = toOrderEntity(createOrderRequest);
        final var savedOrderEntity = orderRepository.save(newOrderEntity);
        //send notification

        return toOrder(savedOrderEntity);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        final var existingOrderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order with (id = {}) is not found", orderId);
                    return new ObjectNotFoundException(String.format("Order with (id = %d) is not found", orderId));
                });

        existingOrderEntity.setStatus(orderStatus);

        log.info("Order with (id = {}) is updated. Updated order: {}", orderId, existingOrderEntity);

        return toOrder(existingOrderEntity);
    }

    @Override
    public Order getOrderById(Long orderId) {
        final var orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order with (id = {}) is not found", orderId);
                    return new ObjectNotFoundException(String.format("Order with (id = %d) is not found", orderId));
                });

        log.info("Order with (id = {}) is fetched. Order: {}", orderId, orderEntity);

        return toOrder(orderEntity);
    }

    private OrderEntity toOrderEntity(CreateOrderRequest createOrderRequest) {
        var orderEntity = new OrderEntity();
        orderEntity.setUserId(createOrderRequest.getUserId());
        orderEntity.setOrderItems(createOrderRequest.getOrderItems()
                .stream()
                .map(item -> {
                    var orderItemEntity = new OrderItemEntity();
                    orderItemEntity.setPizzaId(item.getPizzaId());
                    orderItemEntity.setCount(item.getCount());
                    return orderItemEntity;
                })
                .toList());

        orderEntity.setStatus(OrderStatus.CREATED);

        return orderEntity;
    }

    private Order toOrder(OrderEntity orderEntity) {
        var order = new Order();
        order.setOrderId(orderEntity.getId());
        order.setUserId(orderEntity.getUserId());
        order.setCreated(orderEntity.getCreated());
        order.setUpdated(orderEntity.getUpdated());
        order.setOrderStatus(orderEntity.getStatus());
        order.setOrderItems(orderEntity.getOrderItems()
                .stream()
                .map(item -> {
                    var orderItem = new OrderItem();
                    orderItem.setPizzaId(item.getPizzaId());
                    orderItem.setCount(item.getCount());
                    return orderItem;
                })
                .toList());

        return order;
    }
}
