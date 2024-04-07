## Conditional Property

application.yml에 등록한 설정 값에 따라 Config를 등록하거나 등록하지 않도록 제어하는 예제
`@Profile()` 어노테이션으로도 제어할 수 있으나, 프로파일 활성화를 제외하고 특정 값으로만 제어할 수 있도록 구성한다.

### 1. application.yml 설정 추가

```yaml
myapp:
  feature:
    enabled: true
```

위 설정에서 `myapp.feature.enabled` 값을 `true` 또는 `false`로 설정하여 기능을 활성화하거나 비활성화할 수 있습니다.

### 2. Configuration 클래스에 @ConditionalOnProperty 어노테이션 적용

```java
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
```

`@ConditionalOnProperty` 어노테이션의 `name` 속성에는 확인할 프로퍼티의 이름을 지정하고, `havingValue` 속성에는 조건을 충족하기 위해 프로퍼티가 가져야 하는 값을 지정합니다. `matchIfMissing` 속성을 `false`로 설정하면, 해당 프로퍼티가 정의되지 않았을 때 조건이 충족되지 않도록 합니다(기본값은 `true`).

### 테스트

#### myapp.feature.enabled == true

![스크린샷 2024-04-07 오후 8.52.03.png](src%2Fmain%2Fresources%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-04-07%20%EC%98%A4%ED%9B%84%208.52.03.png)

#### myapp.feature.enabled == false

![스크린샷 2024-04-07 오후 8.53.03 1.png](src%2Fmain%2Fresources%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-04-07%20%EC%98%A4%ED%9B%84%208.53.03%201.png)
