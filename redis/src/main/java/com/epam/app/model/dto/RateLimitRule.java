package com.epam.app.model.dto;

import com.epam.app.validation.OptionalNotBlank;
import com.epam.app.validation.TimeUnitInterval;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class RateLimitRule {
    private String accountId;

    private String clientIp;

    @OptionalNotBlank
    private String requestType;

    @Positive
    private int allowedNumberOfRequests;

    @TimeUnitInterval
    private String timeInterval;

    public TimeUnit getTimeInterval() {
        if (timeInterval == null) {
            return null;
        }
        return TimeUnit.valueOf(timeInterval);
    }
}
