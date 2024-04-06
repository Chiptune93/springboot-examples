package dev.chiptune.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Redis에 문자열 데이터 저장
    public void setStringValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Redis에서 문자열 데이터 검색
    public String getStringValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
