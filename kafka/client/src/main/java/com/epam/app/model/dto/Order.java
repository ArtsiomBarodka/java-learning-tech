package com.epam.app.model.dto;

import com.epam.app.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long orderId;
    private Long userId;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<OrderItem> orderItems;
    private OrderStatus orderStatus;
}
