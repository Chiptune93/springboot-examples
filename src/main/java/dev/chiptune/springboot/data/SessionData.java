package dev.chiptune.springboot.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

// @RedisHash 어노테이션을 사용하여 이 클래스의 인스턴스가 Redis에 저장될 객체임을 선언합니다.
// 'value' 속성으로 Redis에서 사용될 키의 접두사를 'session'으로 설정합니다.
// 'timeToLive' 속성은 객체가 Redis에 저장된 후 자동으로 만료되기까지의 시간(초 단위)을 설정합니다.
// 여기서는 1209600초, 즉 14일로 설정되어 있습니다.
@RedisHash(value = "session", timeToLive = 1209600)
public class SessionData {
    // @Id 어노테이션은 이 필드가 Redis 내에서 각 객체의 고유 식별자 역할을 함을 나타냅니다.
    @Id
    private String id;

    // 세션 ID를 저장하는 필드로, 특별한 인덱싱이나 어노테이션 없이 단순한 문자열 타입의 데이터입니다.
    private String sessionId;

    // @Indexed 어노테이션은 Redis에서 이 필드를 인덱싱하여 빠른 검색이 가능하도록 합니다.
    // 'sessionToken' 필드에 적용되어 있으며, 이 토큰을 통해 효율적인 조회가 가능합니다.
    @Indexed
    private String sessionToken;

    public SessionData(String sessionToken, String sessionId, String id) {
        this.sessionToken = sessionToken;
        this.sessionId = sessionId;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
