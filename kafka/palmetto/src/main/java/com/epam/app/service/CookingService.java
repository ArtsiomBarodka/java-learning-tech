package com.epam.app.service;

import com.epam.app.model.Order;
import org.springframework.lang.NonNull;

public interface CookingService {
    void cook(@NonNull Order order);
}
