package com.epam.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.model.OrderStatus;
import com.epam.app.model.dto.Order;
import com.epam.app.model.dto.OrderItem;
import com.epam.app.model.entity.OrderEntity;
import com.epam.app.model.entity.OrderItemEntity;
import com.epam.app.repository.OrderRepository;
import com.epam.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    @NonNull
    @Transactional
    public Order createOrder(@NonNull Order order) {
        final var newOrderEntity = toOrderEntity(order);
        final var savedOrderEntity = orderRepository.save(newOrderEntity);
        final var savedOrder = toOrder(savedOrderEntity);

        log.info("New order was created: {}", savedOrder);

        return savedOrder;
    }

    @Override
    @NonNull
    @Transactional
    public Order updateOrderStatus(@NonNull Long orderId, @NonNull OrderStatus orderStatus) {
        final var existingOrderEntity = orderRepository.findById(orderId)
                .orElseThrow(trowOrderNotFoundException(orderId));

        existingOrderEntity.setStatus(orderStatus);

        log.info("Order with (id = {}) is updated. Updated order: {}", orderId, existingOrderEntity);

        return toOrder(existingOrderEntity);
    }

    @Override
    @NonNull
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Order getOrderById(@NonNull Long orderId) {
        final var orderEntity = orderRepository.findById(orderId)
                .orElseThrow(trowOrderNotFoundException(orderId));

        log.info("Order with (id = {}) is fetched. Order: {}", orderId, orderEntity);

        return toOrder(orderEntity);
    }

    private OrderEntity toOrderEntity(Order order) {
        var orderEntity = new OrderEntity();
        orderEntity.setUserId(order.getUserId());
        orderEntity.setOrderItems(order.getOrderItems()
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
        return Order.builder()
                .orderId(orderEntity.getId())
                .userId(orderEntity.getUserId())
                .created(orderEntity.getCreated())
                .updated(orderEntity.getUpdated())
                .orderStatus(orderEntity.getStatus())
                .orderItems(orderEntity.getOrderItems()
                        .stream()
                        .map(this::getOrderItemEntityOrderItemFunction)
                        .toList())
                .build();
    }

    private OrderItem getOrderItemEntityOrderItemFunction(OrderItemEntity item) {
        return OrderItem.builder()
                .pizzaId(item.getPizzaId())
                .count(item.getCount())
                .build();
    }

    private Supplier<ObjectNotFoundException> trowOrderNotFoundException(Long orderId) {
        return () -> {
            log.warn("Order with (id = {}) is not found", orderId);
            return new ObjectNotFoundException(String.format("Order with (id = %d) is not found", orderId));
        };
    }
}
