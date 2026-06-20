package br.com.joaodddev.banking.infrastructure.idempotency;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

    private static final String PREFIX = "idempotency:";
    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;

    public IdempotencyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAlreadyProcessed(String idempotencyKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + idempotencyKey));
    }

    public void markAsProcessed(String idempotencyKey) {
        redisTemplate.opsForValue().set(PREFIX + idempotencyKey, "processed", TTL);
    }
}