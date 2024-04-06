package dev.chiptune.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableJdbcRepositories(
        basePackages = {"dev.chiptune.springboot.dataRepo"}
//        , transactionManagerRef = "postgresTransactionManager"
)
public class RedisConfig {

    // @Value 어노테이션을 사용하여 application.properties 파일의
    // Redis 서버 호스트 이름, 포트 번호, 비밀번호 설정 값을 로드합니다.
    @Value("${spring.datasource.redis.host}")
    private String redisHost;

    @Value("${spring.datasource.redis.port}")
    private int redisPort;

    @Value("${spring.datasource.redis.password}")
    private String redisPassword;

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
}


