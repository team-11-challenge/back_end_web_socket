package com.example.pt_websocket.domain.websocket.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, Long> redisTemplateCache;

    // redis 서버확인
    public boolean isRedisDown() {
        try {
            redisTemplateCache.execute((RedisCallback<Object>) connection -> connection.info());
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    // key로 redis에 캐시가 있는지 조회하고 Boolean 반환
    public Boolean hasLeftSeatsInRedis(Long courseId){
        String key = "c" + courseId;
        return redisTemplateCache.hasKey(key);
    }
}
