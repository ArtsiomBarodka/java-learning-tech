package com.epam.app.utils;

import com.epam.app.exception.ValidationException;
import com.epam.app.model.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Validator {
    public void validateMessage(OrderMessage orderMessage) {
        orderMessage.getOrderItems().forEach(item -> {
            if (item.getCount() < 1) {
                log.warn("Pizzas count can't be less than 1! Pizzas count: {}", item.getCount());
                throw new ValidationException("Pizzas count can't be less than 1! Pizzas count: %d".formatted(item.getCount()));
            }
        });
    }
}
