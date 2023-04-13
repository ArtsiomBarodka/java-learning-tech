package com.epam.app.facade;

import com.epam.app.model.OrderMessage;
import org.springframework.lang.NonNull;

public interface CookingFacade {
    void cook(@NonNull OrderMessage orderMessage);
}
