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

### data-jdbc를 사용하기 위한 설정

#### 설정 클래스

- data-jdbc를 사용하기 위해서는 기존 메인 데이터베이스 커넥션에서 제공하는 트랜잭션 매니저가 필요합니다.
- 여기서는 예제이기 때문에 스프링 부트 기본 제공 구성을 사용하기 위해 인메모리 H2 데이터베이스를 사용합니다.
- 만약 별도의 메인 데이터베이스가 있다면 DatabaseConfig를 구현하여 해당 설정 빈에서 사용하는 트랜잭션 매니저를 아래와 같이 별도 지정합니다.
```java
// RedisConfig 클래스에 지정
@EnableJdbcRepositories(
        basePackages = {"{레디스 레파지토리 패키지 경로}"}
        , transactionManagerRef = "{트랜잭션 매니저 빈 이름")
```

#### 엔티티 구성

- 레디스에 넣을 데이터 엔티티를 구성합니다.
- 키에 해당하는 변수를 `@Id` 로 지정해주어야 합니다.
- 그 외에 `getter`, `setter`, `AllArg` 도 존재해야 합니다.

```java
@RedisHash("Token")
public class TestEntity {
    @Id
    String key;
    String value;

    public TestEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
```

#### 레파지토리 생성

- CRUDRepository를 확장하는 인터페이스를 구현합니다.
- 이렇게 하면 RestTemplate이 아닌 CRUDRepository를 이용할 수 있게 됩니다.

```java
public interface RedisRepository extends CrudRepository<TestEntity, String> {
}

```

### 테스트

#### 테스트용 컨트롤러 작성

```java
@PostMapping("/redis-data-jdbc")
    public Iterable<TestEntity> redisDataJdbc() {
        redisRepository.save(new TestEntity("1", "test"));
        return redisRepository.findAll();
    }
```

#### 테스트

![스크린샷 2024-04-07 오전 2.15.55.png](src%2Fmain%2Fresources%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-04-07%20%EC%98%A4%EC%A0%84%202.15.55.png)
