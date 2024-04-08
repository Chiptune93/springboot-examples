## EhCache3 적용하기

Ehcache3 버전부터는 JSR-107을 자바 표준 인터페이스를 구현하였다. 캐시에 대한 자바 표준 인터페이스를 JCache 라고 한다. JPA 구현체에 Hibernate 와 EclipseLink 등이 있는 것
처럼 JCache 구현체 중 하나가 Ehcache3 이다.

### 공식 문서

[https://www.ehcache.org/documentation/3.8/getting-started.html](https://www.ehcache.org/documentation/3.8/getting-started.html)

위 페이지에서 시작 설정하는 법을 참고할 수 있다.
ehCache를 사용하기 위해서 설정하는 방법은 XML과 Java Config 2가지 방식이 존재한다.

### 스프링 부트에 적용하기

스프링 부트는 `@EnableCaching` 어노테이션이 활성화 된 경우, 캐시 빈을 자동으로 구성하도록 되어있다.
[#참고](https://docs.spring.io/spring-framework/docs/5.0.13.RELEASE/spring-framework-reference/integration.html#cache)

특정 캐시 라이브러리를 추가 하지 않으면 기본으로 캐시 매니저를 구성하지만, 운영 환경에서는 권장하지 않는다고 한다.
[#참고](https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-caching.html#boot-features-caching-provider-jcache)

스프링 부트에서는 아래 캐시 매니저를 순서대로 찾으려 하고, 존재하는 경우 해당 캐시 매니저를 구성한다.

1. Generic
2. JCache (JSR-107) (EhCache 3, Hazelcast, Infinispan, and others)
3. EhCache 2.x
4. Hazelcast
5. Infinispan
6. Couchbase
7. Redis
8. Caffeine
9. Simple

여기서 우리가 사용할 것은 EhCache3 이다.

### 의존성 추가하기

```groovy
// ehCache 사용에 필요한 의존성 추가.
implementation("org.springframework.boot:spring-boot-starter-cache")
implementation("org.ehcache:ehcache:3.8.1")
implementation("javax.cache:cache-api:1.1.1")
```

### 설정 구성하기

JavaConfig 방식과 XML 구성 방식이 있다. JavaConfig 보다는 XML로 관리하는 것이 
더 편하다고 보며, 이 설정 파일을 명시적으로 캐시 매니저 빈에서 가져와 사용하도록 구성하는 것이 
그나마 나아 보인다.

#### XML 설정 파일 예시

해당 파일은 `classpath:` 경로인 `resources` 폴더 밑에 위치한다.

**ehcache.xml**
```xml
<config
        xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
        xmlns='http://www.ehcache.org/v3'
        xmlns:jsr107="http://www.ehcache.org/v3/jsr107"
        xsi:schemaLocation="http://www.ehcache.org/v3 http://www.ehcache.org/schema/ehcache-core.xsd
        http://www.ehcache.org/v3/jsr107 http://www.ehcache.org/schema/ehcache-107-ext-3.0.xsd">

    <service>
        <!-- JMX를 통한 캐시 관리 및 통계 수집 활성화 -->
        <jsr107:defaults enable-management="true" enable-statistics="true"/>
    </service>

    <cache alias="UserCache"><!-- 개별 캐시 설정, 캐시의 고유 이름(alias) 지정 -->
        <key-type>java.lang.String</key-type><!-- 캐시 키 타입 지정 -->
        <value-type>dev.chiptune.springboot.entity.Users</value-type><!-- 캐시 값 타입 지정 -->

        <expiry>
            <!-- 캐시 항목의 TTL(Time-To-Live), 30분 후 만료 -->
            <ttl unit="minutes">30</ttl>
        </expiry>

        <listeners>
            <listener>
                <!-- 리스너 클래스 위치 -->
                <class>dev.chiptune.springboot.listener.CustomCacheEventListener</class>
                <!-- 비동기 방식 사용, 캐시 동작을 블로킹하지 않고 이벤트를 처리, SYNCHRONOUS와 반대 -->
                <event-firing-mode>ASYNCHRONOUS</event-firing-mode>
                <!-- 이벤트 처리 순서 설정 X, ORDERED와 반대 -->
                <event-ordering-mode>UNORDERED</event-ordering-mode>
                <!-- 리스너가 감지할 이벤트 설정(EVICTED, EXPIRED, REMOVED, CREATED, UPDATED) -->
                <events-to-fire-on>EVICTED</events-to-fire-on>
                <events-to-fire-on>EXPIRED</events-to-fire-on>
                <events-to-fire-on>REMOVED</events-to-fire-on>
                <events-to-fire-on>CREATED</events-to-fire-on>
                <events-to-fire-on>UPDATED</events-to-fire-on>
            </listener>
        </listeners>

        <resources>
            <!-- JVM 힙 메모리에 캐시 저장 -->
            <heap unit="entries">100</heap>
            <!-- off-heap(외부 메모리)에 캐시 저장 -->
            <offheap unit="MB">10</offheap>
        </resources>

    </cache>

</config>


```

application.yml에도 해당 설정 파일의 위치를 알려주는 설정을 추가한다.

```yml
spring:
  cache:
    jcache:
      config: classpath:ehcache.xml
```

그리고 `@EnableCaching` 어노테이션을 추가한다.

### 리스너 등록

캐시작업이 일어날 때, 이를 모니터링 할 이벤트 리스너를 등록한다.

```java
public class CustomCacheEventListener implements CacheEventListener<Object, Object> {

    private static final Logger logger = LoggerFactory.getLogger(CustomCacheEventListener.class);

    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        logger.info("━━━━━━━━━━━━━━━━━BaseCacheEventListener START━☂☄");
        logger.info("☆ﾟ.*･｡ﾟ Cache Key : " + cacheEvent.getKey() + " ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
        logger.info("☆ﾟ.*･｡ﾟ Cache Old Value : " + cacheEvent.getOldValue() + " ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
        logger.info("☆ﾟ.*･｡ﾟ Cache New Value : " + cacheEvent.getNewValue() + " ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
        logger.info("━━━━━━━━━━━━━━━━━BaseCacheEventListener END━☂☄");
    }
}
```

---

### 주의사항!

위 단계까지만 해도 기본적으로 ehcache를 사용할 수 있다.
만약, 프로젝트에 redis를 이미 사용 중이라면 redis가 Ehcache보다 우선순위가 높아 기본 CacheManger를 RedissonSpringCacheManager로 설정한다.

따라서, 따로 Ehcache용 CacheManager를 @Bean으로 등록해주어야 한다. 
여기서 중요한것은 org.springframework.cache.CacheManger 인터페이스의 구현체로 
org.springframework.cache.ehcache.EhCacheCacheManager 가 있는데, 이 클래스는 Ehcache2 버전에 사용되는 것이다. 
처음에도 말했듯이 Ehcache3는 JCache의 한 종류로 JCacheCacheManger로 등록해줘야한다.

```java
import java.io.IOException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class EhcacheConfig {

	@Bean(name = "ehCacheManager")
	public org.springframework.cache.CacheManager cacheManager() throws IOException {
		CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
		CacheManager manager = cachingProvider.getCacheManager(
				new ClassPathResource("/ehcache.xml").getURI(),
				getClass().getClassLoader());

		return new JCacheCacheManager(manager);
	}
}
```

위 내용 출처 및 참고 [https://px201226.github.io/ehcache3/](https://px201226.github.io/ehcache3/)

---

### 테스트

샘플로 생성한 `UserService`에 캐시를 지정한다.

```java
@Service
public class UserService {
    final
    UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Cacheable(
            value = "UserCache",
            key = "#id"
    )
    public Users getUser(Long id) {
        return userRepo.findById(id).get();
    }

}

```

유저 하나를 조회할 때 이 결과를 캐싱한다. 키 값은 메소드 파라미터인 id로 잡았다.

```java

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @DisplayName("캐싱 테스트")
    @Test
    public void cachingTest() {
        userService.getUser(1L);
    }

}
```

테스트에서 메소드를 호출해본다.

```bash
2024-04-08 23:43:52.095  INFO 12616 --- [    Test worker] h2database                               : jdbc[3] 
/*SQL l:159 #:1*/SELECT \"USERS\".\"ID\" AS \"ID\", \"USERS\".\"EMAIL\" AS \"EMAIL\", \"USERS\".\"PASSWORD\" AS \"PASSWORD\", \"USERS\".\"USERNAME\" AS \"USERNAME\" FROM \"USERS\" WHERE \"USERS\".\"ID\" = ? {1: CAST(1 AS BIGINT)};
2024-04-08 23:43:52.100  INFO 12616 --- [    Test worker] h2database                               : jdbc[3] 
/*SQL #:1*/CALL DATABASE();
2024-04-08 23:43:52.106  INFO 12616 --- [    Test worker] h2database                               : jdbc[3] 
/*SQL */COMMIT;
2024-04-08 23:43:52.106  INFO 12616 --- [    Test worker] h2database                               : jdbc[3] 
/*SQL */COMMIT;
2024-04-08 23:43:52.111  INFO 12616 --- [e [_default_]-0] d.c.s.listener.CustomCacheEventListener  : ━━━━━━━━━━━━━━━━━BaseCacheEventListener START━☂☄
2024-04-08 23:43:52.112  INFO 12616 --- [e [_default_]-0] d.c.s.listener.CustomCacheEventListener  : ☆ﾟ.*･｡ﾟ Cache Key : 1 ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹
2024-04-08 23:43:52.112  INFO 12616 --- [e [_default_]-0] d.c.s.listener.CustomCacheEventListener  : ☆ﾟ.*･｡ﾟ Cache Old Value : null ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹
2024-04-08 23:43:52.116  INFO 12616 --- [e [_default_]-0] d.c.s.listener.CustomCacheEventListener  : ☆ﾟ.*･｡ﾟ Cache New Value : Users(id=1, username=user1, email=user1@example.com, password=pass1) ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹
2024-04-08 23:43:52.116  INFO 12616 --- [e [_default_]-0] d.c.s.listener.CustomCacheEventListener  : ━━━━━━━━━━━━━━━━━BaseCacheEventListener END━☂☄
> Task :test
2024-04-08 23:43:52.121  INFO 12616 --- [       Thread-4] h2database                               : database closing mem:h2-test from shutdown hook
```

캐시가 정상 동작하는 것을 확인할 수 있다.
