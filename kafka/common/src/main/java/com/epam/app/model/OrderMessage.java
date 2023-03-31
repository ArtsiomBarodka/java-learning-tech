package com.epam.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    private Long orderId;
    private Set<OrderItemMessage> orderItems;
}
