package com.epam.app.service.impl;

import com.epam.app.model.Order;
import com.epam.app.model.OrderItem;
import com.epam.app.service.CookingService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Slf4j
@Service
public class CookingServiceImpl implements CookingService {
    @Override
    public void cook(Order order) {
        log.info("Starting cooking the order: {} ", order);
        order.getOrderItems().forEach(this::cook);
        log.info("Finished cooking the order: {} ", order);
    }

    @SneakyThrows
    private void cook(OrderItem orderItem) {
        for (int i = 1; i <= orderItem.getCount(); i++) {
            log.info("Cooking the {} pizza with id {}", i, orderItem.getPizzaId());
            sleep(3000);
            log.info("Finished cooking the {} pizza with id {}", i, orderItem.getPizzaId());
        }
    }
}
