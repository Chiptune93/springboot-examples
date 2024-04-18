package dev.chiptune.springboot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.chiptune.springboot.data.SessionData;
import dev.chiptune.springboot.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisServiceTest {

    @MockBean
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private ValueOperations<String, String> valueOperations;

    @Autowired
    @InjectMocks // RedisService 내부의 MockBean을 자동으로 주입
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        // StringRedisTemplate 목 객체의 opsForValue() 메서드가 호출될 때 valueOperations 목 객체를 반환하도록 설정
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void setStringValueTest() {
        // 실행
        redisService.setStringValue("testKey", "testValue");

        // 검증: valueOperations의 set 메서드가 "testKey", "testValue"로 호출되었는지 검증
        verify(valueOperations).set("testKey", "testValue");
    }

    @Test
    void getStringValueTest() {
        // 준비: "testKey"에 대해 "testValue"를 반환하도록 설정
        when(valueOperations.get("testKey")).thenReturn("testValue");

        // 실행
        String result = redisService.getStringValue("testKey");

        // 검증: 결과값이 "testValue"인지 확인
        assert result.equals("testValue");

        // 검증: valueOperations의 get 메서드가 "testKey"로 호출되었는지 검증
        verify(valueOperations).get("testKey");
    }

    @Test
    public void testFindBySessionId() {
        // 세션 데이터 예제 생성
        SessionData expectedSession = new SessionData("token_123", "123", "1");

        // 세션 ID로 세션 데이터 조회 시, 설정된 객체를 반환하도록 설정
        when(redisService.getSessionData("123")).thenReturn(expectedSession);

        // 실제 테스트 실행
        SessionData result = redisService.getSessionData("123");

        // 검증
        assertNotNull(result);
        assertEquals("123", result.getSessionId());
        assertEquals("token_123", result.getSessionToken());
    }
}
