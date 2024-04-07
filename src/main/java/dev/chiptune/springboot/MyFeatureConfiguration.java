package dev.chiptune.springboot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "myapp.feature.enabled", havingValue = "true", matchIfMissing = false)
public class MyFeatureConfiguration {

    @Bean
    public MyFeature myFeature() {
        // MyFeature 인스턴스 생성 로직
        return new MyFeature();
    }
}
