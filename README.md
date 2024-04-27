## Springboot Error Handling

시큐리티가 적용된 스프링 부트에서 에러 처리를 전체적으로 해보자.

### Security Error Handling

시큐리티에러 핸들링은 이전 글에서 다루었다. [여기](https://chiptune93.github.io/backend/spring/spring-security-pratice4.html)
를 참고하면 된다. 간단하게 다시 설명하면 아래와 같다.

#### 인증 과정에서의 에러 발생 핸들링

- CustomAuthenticationEntryPoint

`AuthenticationEntryPoint` 를 구현하여 등록한다. 인증 과정에서 에러가 발생시 해당 함수를 실행하여 처리한다.


```java
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 주석 부분은 단순히 에러 응답만 보낸다.
        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "커스텀 에러 핸들링! 인증이 필요합니다!");
        response.sendRedirect("/error/login-required");
    }
}
```

#### 권한 처리에서의 액세스 에러 핸들링

- CustomAccessDeniedHandler

인증 후, 권한이 없는 리소스에 접근 시 해당 에러 핸들러를 통해 처리한다.

```java
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 주석 부분은 단순히 에러 응답만 보낸다.
        // response.sendError(HttpServletResponse.SC_FORBIDDEN, "커스텀 액세스 핸들링! 권한이 없습니다!");
        response.sendRedirect("/error/access-denied");
    }
}
```

### ControllerAdvice Error Handling

시큐리티 인증 과정 혹은 권한과 연계된 에러가 발생 시, 위 시큐리티 설정에 등록한 핸들러를 통해 에러 처리가 가능하다.
또는 시큐리티에서 발생한 에러를 `ControllerAdvice`에 넘겨서 처리할 수도 있다. 

여기서는 시큐리티에서 발생한 에러를 처리하는 것과 그 외에 에러 발생 시 에러를 넘겨받아 공통 에러 처리 클래스에서 처리하는 방법을 소개한다.

#### `ControllerAdvice`

@ControllerAdvice는 Spring MVC의 컨트롤러를 보조하는 역할을 합니다. 주로 다음과 같은 공통적인 처리를 구현하는 데 사용됩니다

- 예외 처리: 애플리케이션에서 발생할 수 있는 예외를 일관된 방식으로 처리합니다. @ExceptionHandler 어노테이션과 결합하여 특정 예외에 대한 처리 로직을 구현할 수 있습니다.
- 데이터 바인딩: @InitBinder를 사용하여 요청에서 넘어온 데이터를 컨트롤러에서 사용하기 전에 커스텀 바인딩을 설정할 수 있습니다.
- 모델 특성 추가: @ModelAttribute 어노테이션을 이용해 모든 컨트롤러의 모델 객체에 자동으로 추가되어야 하는 속성 값을 설정할 수 있습니다.


#### `RestControllerAdvice`

@RestControllerAdvice는 @ControllerAdvice에 @ResponseBody가 적용된 형태입니다. 주로 REST API를 개발할 때 사용되며, JSON이나 XML 같은 응답 바디를 직접 조작할 수 있습니다. @RestControllerAdvice는 다음과 같은 용도로 사용됩니다:

예외 처리: REST API에서 발생하는 예외를 HTTP 응답으로 직접 매핑하여 처리합니다. @ExceptionHandler와 함께 사용하여 HTTP 응답 바디에 에러 정보를 JSON 등의 포맷으로 전송할 수 있습니다.


#### 공통 에러 처리 클래스로 에러 처리하기

공통 에러 처리클래스에서 특정 에러를 지정하여 핸들링 하기 위해 일반적인 `Exception` 에러를 확장하는 커스텀 에러 클래스를 생성한다.

- CustomNotFoundException

일반적인 상황의 에러 발생.

```java
public class CustomNotFoundException extends Exception {
    public CustomNotFoundException(String message) {
        super("Custom Not Found!\n" + message);
    }
}
```

- RestCustomNotFoundException

REST API 요청 상황에서 에러 발생.

```java
public class RestCustomNotFoundException extends Exception {
    public RestCustomNotFoundException(String message) {
        super("Rest Custom Not Found!\n" + message);
    }
}
```

그리고 이 에러를 핸들링하는 컨트롤러 어드바이스를 등록한다.

- CustomControllerAdvice

```java

@ControllerAdvice
public class CustomControllerAdvice {
    // 커스텀 예외를 핸들링하는 메소드
    @ExceptionHandler(CustomNotFoundException.class)
    public String handleCustomNotFoundException(Model model, CustomNotFoundException ex) {
        model.addAttribute("error", ex.getMessage());
        return "/error/CustomNotFound";
    }
}

```

- RestCustomControllerAdvice

```java
@RestControllerAdvice
public class CustomRestControllerAdvice {

    // 사용자 정의 예외 핸들링
    @ExceptionHandler(RestCustomNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            CustomNotFoundException ex, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
```

여기서 예제에는 `Thymeleaf` 를 사용하기 때문에 에러 페이지에서 메세지를 보여주기 위해 `Model`을 받아서
메세지를 등록하고 지정된 에러 페이지로 이동하도록 세팅했다.

그리고 REST API 상황에서 에러 리턴을 하기 위해 `RestControllerAdvice` 
같은 방식으로 예제에서는 커스텀 에러 응답을 리턴하도록 등록한다.

- ErrorDetails 

커스텀 에러 응답을 보내주기 위한 클래스.

```java
// 에러 디테일을 담을 클래스
@Getter
public class ErrorDetails {
    // getter 메소드들
    private int statusCode;
    private String message;
    private String details;

    public ErrorDetails(int statusCode, String message, String details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }
}
```

그리고 강제로 에러를 발생 시켜 테스트 하기 위해 컨트롤러를 등록한다.

- Controller에 강제 에러 발생

```java
@Controller
@RequestMapping("/error")
public class ForceExceptionController {

    @GetMapping("/forceException")
    String customNotFoundException(Model model) throws CustomNotFoundException {
        throw new CustomNotFoundException("Force Exception!");
    }

}

// ...
// ...

@RestController
@RequestMapping("/error")
public class RestForceExceptionController {

    @PostMapping("/forceException")
    public String customNotFoundException() throws RestCustomNotFoundException {
        throw new RestCustomNotFoundException("Rest Force Exception!");
    }
}
```

#### Controller Advice 테스트

- `localhost:8080/error/forceException` 으로 접속하여 에러페이지로 넘어가는 지 확인!

![img.png](img.png)

- `localhost:8080/error/forceException` 으로 POST 요청을 보내어 구현한 커스텀 에러가 발생하는 지 확인!

![img_1.png](img_1.png)

두 요청 모두 성공적으로 지정한 에러를 응답하는 것을 확인할 수 있다.


#### Security 에서 발생한 에러를 넘겨받아 처리하기

시큐리티에서 에러처리를 따로 하고, ControllerAdvice에서 에러를 따로 처리하는 방식이 마음에 들지 않고
한 곳에서 처리를 하고 싶다면 조금은 번거롭지만 방법이 있긴 하다.

JWT 토큰 인증 같이 `OnceRequestFilter` 에서 구현되는 부분은 [여기](https://colabear754.tistory.com/172#@ControllerAdvice%EC%97%90%EC%84%9C_%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0_%EC%98%88%EC%99%B8%EB%A5%BC_%EC%B2%98%EB%A6%AC%ED%95%98%EA%B3%A0_%EC%8B%B6%EB%8B%A4!)
를 참고하면 더 자세히 설명하고 있다.

게다가 이 예제의 경우 폼 로그인을 다루고 있기 때문에 더 번거롭게 작업을 해야한다.

우선, 폼 로그인 필터에 로그인 실패시 핸들러를 추가한다.

- CustomAuthenticationFailureHandler

```java

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 폼 로그인에서 사용할 실패 핸들러
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        if (exception instanceof BadCredentialsException) {
            // 지정한 컨트롤러로 보낸다.
            response.sendRedirect("/error/BadCredentialsException");
        }
        // 추가적인 예외 처리 로직을 구현
    }
}
```

여기서 리다이렉트 하는 엔드포인트는 강제로 해당 에러를 발생시키는 엔드포인트이다.

```java
@GetMapping("/BadCredentialsException")
    String badCredentialsException(Model model) throws BadCredentialsException {
        throw new BadCredentialsException("Force BadCredentialsException!");
    }
```

실패 핸들러를 만들었으면, 필터에 등록한다. 기존에 등록된 로그인 페이지로 보내는 부분을 주석처리하고
새로 만든 핸들러를 등록한다.

```java
@Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager()); // 인증 관리자를 설정합니다.
        filter.setFilterProcessesUrl("/loginProc"); // 인증 처리 URL을 설정합니다.
        filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler()); // 인증 성공 핸들러를 설정합니다.
        // filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error")); // 인증 실패 핸들러를 설정합니다.
        filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler()); // ControllerAdvice에서 처리하기 위한 핸들러 등록
        return filter; // CustomAuthenticationFilter 인스턴스를 반환합니다.
    }
```

이런 식으로 처리하게 되면 폼 로그인 과정에서 해당 에러가 발생하게 되면 강제로 동일한 에러가 발생하는 컨트롤러 엔드포인트로 이동하고
해당 컨트롤러로 이동했다는 것은 필터의 범위를 넘어서 애플리케이션으로 요청이 들어갔기 때문에 `ControllerAdvice` 에서 에러를 캐치할 수 있다.

- ControllerAdvice

```java
import dev.chiptune.springboot.error.CustomNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {

    // 커스텀 예외를 핸들링하는 메소드
    @ExceptionHandler(CustomNotFoundException.class)
    public String handleCustomNotFoundException(Model model, CustomNotFoundException ex) {
        model.addAttribute("error", ex.getMessage());
        return "/error/CustomNotFound";
    }

    // 시큐리티 인증 과정의 에러를 처리하기 위한 핸들링 옵션
    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException e) {
        // 로그인 실패 시 응답
        return "/error/FormLoginSecurityError";
    }
}

```

만약 해당 부분을 일반 `ControllerAdvice` 가 아닌 `RestControllerAdvice`로 등록하게되면
폼 로그인의 경우, 실패시 이동할 페이지가 지정되어야 하기 때문에 에러가 발생하면 `WhiteLabelPage`가 나오게된다.

따라서, 컨트롤러 어드바이스에서 폼 로그인 에러를 처리하고자 한다면 페이지로 이동할 수 있는 `ControllerAdvice` 에서
페이지로 이동되도록 처리해야 한다.

#### 테스트

폼 로그인 과정에서 에러 발생 시, `ControllerAdvice` 를 통해 지정된 에러 페이지로 이동되는지 확인한다.

![img_2.png](img_2.png)

일부러 잘못된 아이디/패스워드를 입력하여 에러를 발생시킨다.

![img_3.png](img_3.png)

지정한 에러 페이지로 이동한다.


### Springboot AutoConfiguration Error Handling 

기본적으로 여기서 처리한 에러 방식들은 실제로 존재하는 URL에 대하여 요청 시 발생하는 에러들이다.
시큐리티의 경우, 필터를 통해 요청이 들어왔을 때 과정에서 에러가 발생하면 처리하고
`ControllerAdvice` 의 경우에는 어플리케이션 내 요청이 들어와서 수행 중 발생하는 에러에 대해 처리를 한다.

그 외에 아예 URL이 없거나(404) 다른 문제로 인하여 위 에러 처리들이 동작하지 않는 경우 
에러 처리가 별도로 필요하다.

스프링 부트에서는 기본적으로 에러 페이지를 HTTP 상태 코드에 맞게 에러 페이지를 기본으로 제공할 수 있도록 세팅할 수 있다.
이는 공식문서에도 나와있으며 [여기](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet.spring-mvc.error-handling.error-pages)
를 참고하면 된다.

간략하게는 `resources` 폴더 하위에 상태코드에 맞는 html 페이지 및 리소스 페이지가 존재하면 된다.

![img_4.png](img_4.png)

위 처럼 404 페이지를 만들어 위치시켜보았다.
그리고 위 에러 처리가 걸리지 않고 404가 발생하게끔 로그인 후, 존재하지 않는 URL에 대해 요청을 해보았다.

![img_5.png](img_5.png)

다음과 같이 만든 에러 페이지로 잘 이동되는 것을 볼 수 있다.

### 결론

이번에는 스프링 시큐리티 에러 처리와 `ControllerAdvice` 를 통한 전역 에러 관리 
그리고 스프링 부트의 기본 에러 처리 페이지 방식을 이용해 전체적으로 에러를 처리할 수 있도록 구성해보았다.

이 정도만 구성하더라도 왠만한 에러 케이스는 커버할 수 있고 기본 화이트 라벨 에러 페이지가
사용자에게 노출되지 않도록 구성할 수 있다.

사용된 전체 예제 코드는 [여기](https://github.com/Chiptune93/springboot.java.example/tree/feature/spring-security/security-entire-error-handling)
에서 확인할 수 있다.
