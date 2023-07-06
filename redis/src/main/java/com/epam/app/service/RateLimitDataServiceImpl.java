package com.epam.app.service;

import com.epam.app.model.request.DescriptorListRequest;
import com.epam.app.repository.RateLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimitDataServiceImpl implements RateLimitDataService {
    private final RateLimitRepository rateLimitRepository;
    @Override
    public @NonNull Long increaseRequestCount(@NonNull DescriptorListRequest.DescriptorRequest descriptor, @NonNull TimeUnit timeUnit) {
        String key = generateRedisKey(descriptor);
        var newRequestCount = rateLimitRepository.incrementCount(key, timeUnit);
        return newRequestCount;
    }

    private String generateRedisKey(DescriptorListRequest.DescriptorRequest descriptor) {
        StringBuilder keyBuilder = new StringBuilder("ratelimit:");
        if (descriptor.getAccountId() != null) {
            keyBuilder.append("accountId:").append(descriptor.getAccountId()).append(":");
        }
        if (descriptor.getClientIp() != null) {
            keyBuilder.append("clientIp:").append(descriptor.getClientIp()).append(":");
        }
        if (descriptor.getRequestType() != null) {
            keyBuilder.append("requestType:").append(descriptor.getRequestType()).append(":");
        }
        return keyBuilder.toString();
    }
}
