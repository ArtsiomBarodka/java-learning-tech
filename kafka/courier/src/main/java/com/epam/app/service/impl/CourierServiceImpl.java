package com.epam.app.service.impl;

import com.epam.app.model.Order;
import com.epam.app.service.CourierService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Slf4j
@Service
public class CourierServiceImpl implements CourierService {
    @SneakyThrows
    @Override
    public void deliverOrder(Order order) {
        log.info("Starting delivering the order: {} ", order);
        sleep(5000);
        log.info("Finished delivering the order: {} ", order);
    }
}
