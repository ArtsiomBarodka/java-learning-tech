package com.epam.app.service;

import com.epam.app.model.request.DescriptorListRequest;
import org.springframework.lang.NonNull;

import java.util.concurrent.TimeUnit;

public interface RateLimitDataService {
    @NonNull Long increaseRequestCount(@NonNull DescriptorListRequest.DescriptorRequest descriptor, @NonNull TimeUnit timeUnit);
}
