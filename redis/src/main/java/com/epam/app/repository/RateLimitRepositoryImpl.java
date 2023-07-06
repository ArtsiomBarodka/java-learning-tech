package com.epam.app.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RateLimitRepositoryImpl implements RateLimitRepository {
    private final RedisTemplate<String, Long> redisTemplate;
    @Override
    public @NonNull Long incrementCount(@NonNull String key, @NonNull TimeUnit timeUnit) {
        var increment = redisTemplate.opsForValue().increment(key);

        var currentExpire = redisTemplate.getExpire(key);
        if (currentExpire != null && currentExpire == -1){
            redisTemplate.expire(key, 1, timeUnit);
        }

        return increment;
    }
}
