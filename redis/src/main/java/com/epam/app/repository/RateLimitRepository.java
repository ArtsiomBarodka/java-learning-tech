package com.epam.app.repository;

import org.springframework.lang.NonNull;

import java.util.concurrent.TimeUnit;

public interface RateLimitRepository {
    @NonNull Long incrementCount(@NonNull String key, @NonNull TimeUnit timeUnit);
}
