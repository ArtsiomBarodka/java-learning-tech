package com.epam.app.facade;

import com.epam.app.model.NotificationMessage;
import org.springframework.lang.NonNull;

public interface CourierFacade {
    void deliver(@NonNull NotificationMessage notificationMessage);
}
