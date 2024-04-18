## Redis

### Redis 설치

docker redis 설치 및 실행

```bash
docker run redis --name redis -d -p 6379:6379 redis
```

redis password 설정

```bash
# 컨테이너 접속
dk@Chiptune ~ % docker exec -it redis bash
# redis-cli로 접속
root@478e4e66d5f8:/data# redis-cli
# 비밀번호 테스트
127.0.0.1:6379> AUTH 'test'
# 비밀번호 미설정 시 멘트
(error) ERR AUTH <password> called without any password configured for the default user. Are you sure your configuration is correct?
# 비밀번호 설정 조회
127.0.0.1:6379> config get requirepass
1) "requirepass"
2) "" # 없음.
# 비밀번호를 1q2w3e4r 로 설정
127.0.0.1:6379> config set requirepass 1q2w3e4r
OK
# 테스트 데이터 세팅
127.0.0.1:6379> set foo boo
OK
# 재접속
127.0.0.1:6379> exit
root@478e4e66d5f8:/data# redis-cli
# 테스트
127.0.0.1:6379> get foo '1q2w3e4r'
# 비번 없어서 실패
(error) ERR wrong number of arguments for 'get' command
# 비번 인증!
127.0.0.1:6379> AUTH '1q2w3e4r'
OK
# 성공
127.0.0.1:6379> get foo
"boo"
```

### Redis Config 설정

- 연결 객체 및 해당 연결을 사용하는 레디스 템플릿 설정

```java
@Bean
// RedisConnectionFactory 빈을 생성합니다. 이 빈은 Redis 서버에 연결하기 위한 커넥션 팩토리 역할을 합니다.
public RedisConnectionFactory redisConnectionFactory() {
    // RedisStandaloneConfiguration 객체를 생성하여 Redis 서버의 호스트 이름, 포트, 비밀번호를 설정합니다.
    RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
    redisConfiguration.setHostName(redisHost);
    redisConfiguration.setPort(redisPort);
    redisConfiguration.setPassword(redisPassword);
    // LettuceConnectionFactory를 사용하여 RedisStandaloneConfiguration 설정을 적용합니다.
    // Lettuce는 Redis를 위한 비동기 클라이언트 라이브러리 중 하나입니다.
    return new LettuceConnectionFactory(redisConfiguration);
}

@Bean
// RedisTemplate 빈을 생성합니다. 이 빈은 Redis 데이터 액세스 코드에서 사용되며,
// Redis 커맨드 실행을 위한 고수준 추상화를 제공합니다.
public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    // 위에서 생성한 RedisConnectionFactory를 RedisTemplate에 설정합니다.
    template.setConnectionFactory(redisConnectionFactory());
    // Redis의 Key와 Value를 직렬화/역직렬화하기 위한 Serializer를 설정합니다.
    // Key는 StringRedisSerializer를 사용하여 처리합니다.
    template.setKeySerializer(new StringRedisSerializer());
    // Value는 Object 타입으로, GenericToStringSerializer를 사용하여 처리합니다.
    // GenericToStringSerializer는 Java 객체를 문자열로 직렬화/역직렬화하는 일반적인 방법을 제공합니다.
    template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
    return template;
}
```


### 테스트

#### 테스트 메소드를 통한 확인

```java
@SpringBootTest
public class RedisServiceTest {

    @MockBean
    private StringRedisTemplate stringRedisTemplate;

    @Mock
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
}
```

#### 테스트용 컨트롤러 작성

```java
@PostMapping("/redis")
    public String redis() {
        redisService.setStringValue("test", "testStringValue");
        return redisService.getStringValue("test");
    }
```

#### 테스트

![스크린샷 2024-04-07 오전 2.18.58.png](src%2Fmain%2Fresources%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-04-07%20%EC%98%A4%EC%A0%84%202.18.58.png)

### 24.04.19 추가, @EnableRedisRepositories 를 이용한 CrudRepository 사용.

- Config 클래스에 `@EnableRedisRepositories` 추가.

```java
@EnableRedisRepositories
public class RedisConfig { ... }
```
 
#### Redis에 저장할 객체 클래스 생성

```java
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
}
```

#### CrudRepository를 확장하는 인터페이스 레파지토리 생성

```java
public interface SessionRepository extends CrudRepository<SessionData, String> {
    SessionData findBySessionId(String sessionId);
}
```

#### 해당 레파지토리를 이용하여 메소드 사용

```java
// CrudRepository 사용
    public SessionData setSessionData(SessionData sessionData) {
        return sessionRepository.save(sessionData);
    }

    // CrudRepository 사용
    public SessionData getSessionData(String id) {
        return sessionRepository.findBySessionId(id);
    }
```
