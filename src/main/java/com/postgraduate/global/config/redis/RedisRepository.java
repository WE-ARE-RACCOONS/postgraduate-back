package com.postgraduate.global.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final HashOperations<String, String, String> hashOps;

    public void setValue(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public Optional<String> getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return Optional.ofNullable(values.get(key));
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public Optional<String> checkBlackList(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String isBlackList = values.get("blackList:" + token);
        return Optional.ofNullable(isBlackList);
    }
}