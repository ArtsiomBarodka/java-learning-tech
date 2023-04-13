package com.epam.app.service;

import com.epam.app.model.Order;
import org.springframework.lang.NonNull;

public interface CourierService {
    void deliverOrder(@NonNull Order order);
}
