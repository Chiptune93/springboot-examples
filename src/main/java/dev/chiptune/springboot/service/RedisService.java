package dev.chiptune.springboot.service;

import dev.chiptune.springboot.data.SessionData;
import dev.chiptune.springboot.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final SessionRepository sessionRepository;

    @Autowired
    public RedisService(StringRedisTemplate redisTemplate, SessionRepository sessionRepository) {
        this.redisTemplate = redisTemplate;
        this.sessionRepository = sessionRepository;
    }

    // Redis에 문자열 데이터 저장
    public void setStringValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Redis에서 문자열 데이터 검색
    public String getStringValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // CrudRepository 사용
    public SessionData setSessionData(SessionData sessionData) {
        return sessionRepository.save(sessionData);
    }

    // CrudRepository 사용
    public SessionData getSessionData(String id) {
        return sessionRepository.findBySessionId(id);
    }


}
