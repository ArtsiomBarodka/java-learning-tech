package com.epam.app.utils;

import com.epam.app.exception.ValidationException;
import com.epam.app.model.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Validator {
    public void validateMessage(NotificationMessage notificationMessage) {
        if (notificationMessage.getUserId() < 1) {
            log.warn("Users id can't be less than 1! Users id: {}", notificationMessage.getUserId());
            throw new ValidationException("Users id can't be less than 1! Users id: %d".formatted(notificationMessage.getUserId()));
        }
    }
}
