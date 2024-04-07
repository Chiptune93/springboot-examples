## Simple Form Login

## Form Login 방식에 대한 이해

![Pasted image 20240407220919.png](src%2Fmain%2Fresources%2FPasted%20image%2020240407220919.png)

기본적으로 폼 로그인을 사용하게 되면, 스프링 시큐리티는 자체적으로 또는 지정한 로그인 페이지에서 Form Submit한 action URL을 캐치하여 시큐리티에서 처리하도록 유도한다.

해당 요청을 필터체인에서는 `UsernamePasswordAuthenticationFilter`에 보내어, 해당 필터 구간에서 인증 처리를 하도록한다.
### UsernamePasswordAuthenticationFilter
시큐리티에서 설정한 폼 로그인은 해당 필터를 무조건 거쳐가게 된다.
따라서, 이 필터에서 사용자 인증 요청을 구현해야 한다.

여기서 인증 요청을 구현해야 된다는 말은, 실제 "인증 처리" 를 해당 필터에서 하는게 아니라 "인증 요청" 을 생성해서 처리를 다른 쪽으로 위임해야 한다는 뜻이다.

실제 해당 필터에서는 `UsernamePasswordAuthenticationToken` 이라는 "인증 요청"을 생성한다. 그러면, 필터는 해당 요청을 `AuthenticationManager` 라고 하는 인증 매니저에게 해당 요청을 넘기게 되고, 인증 매니저는 적절한(해당 요청을 처리할 수 있는) `AuthenticationProvider` 라는 인증 공급자를 찾아 해당 객체에게 실제 인증을 위임하게 된다.

만약 시큐리티에 별도의 `AuthrnticationManager` 가 등록되어 있지 않다면 기본 구성으로 처리를 하게 되는데  [`DaoAuthenticationProvider`](https://docs.spring.io/spring-security/reference/5.7/servlet/authentication/passwords/dao-authentication-provider.html#servlet-authentication-daoauthenticationprovider)에 의해 `UserDetailsService의` `loadByUsername이` 호출되면서 기본 처리를 하게 된다.

제출한 요청이 스프링 시큐리티 구성 시에 로컬 환경에서 사용할 수 있도록 제공하는 임시 패스워드를 입력한 것이 아니라면 요청은 실패하게 된다.

그렇다는 것은 만약, 별도의 필터를 구현할 필요가 없다면(인증 과정에서 별도로 커스터마이징 구간이 없다면) 실제 필터부터 구현할 필요 없이 기본적인 `UserDetailsService의` `loadByUsername만` 재정의할 수 있다면 굳이 다른 부분을 구현하지 않고도 인증 처리를 할 수 있다는 이야기 이다.
## 간단한 Form Login 구현
그렇다면 위에서 언급한대로, 최대한 구현하지 않고 간단하게 폼 로그인을 구성해보자.
프로젝트에서는 인메모리 `H2 Database`와 화면 표현을 위한 `Thymeleaf` 를 사용하였다.
그리고 데이터베이스 연결 및 데이터를 가져오기 위해 `data-jpa`를 사용한다.
### Security Config
```java
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // CSRF(Cross-Site Request Forgery) 보호 기능 설정
        .csrf(csrf -> csrf
            .ignoringAntMatchers("/h2-console/**") // "/h2-console/**" 패턴에 해당하는 경로에 대해 CSRF 보호를 비활성화
        )
        // CORS(Cross-Origin Resource Sharing) 설정을 비활성화
        .cors().disable()
        // 기본 HTTP 인증 방식을 비활성화
        .httpBasic().disable()
        // 보안 헤더 중 "X-Frame-Options"를 비활성화하여 iframe 내에서 페이지를 표시할 수 있도록 함. 주로 H2 데이터베이스 콘솔 같은 내부 도구에 사용
        .headers().frameOptions().disable()
        .and()
        // 폼 로그인 설정
        .formLogin(form -> form
            .loginPage("/login") // 사용자 정의 로그인 페이지 URL 설정
            .defaultSuccessUrl("/home") // 로그인 성공 시 이동할 기본 URL 설정
            .failureUrl("/login") // 로그인 실패 시 이동할 URL 설정
            .loginProcessingUrl("/loginProc") // 로그인 폼 제출 URL 설정
            // 로그인 성공 핸들러 설정
            .successHandler(
                new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication : " + authentication.getName());
                        response.sendRedirect("/home"); // 로그인 성공 시 "/home"으로 리다이렉트
                    }
                }
            )
            // 로그인 실패 핸들러 설정
            .failureHandler(
                new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("exception : " + exception.getMessage());
                        response.sendRedirect("/login"); // 로그인 실패 시 "/login"으로 리다이렉트
                    }
                }
            )
            .permitAll() // 로그인 페이지와 로그인 실패 페이지는 인증 없이 접근 가능하도록 허용
        )
        // 로그아웃 설정
        .logout()
        .and()
        // HTTP 요청에 대한 접근 제어 설정
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(new AntPathRequestMatcher("/sample")).permitAll() // "/sample" 경로는 인증 없이 접근 허용
            .requestMatchers(new AntPathRequestMatcher("/login")).permitAll() // "/login" 경로도 인증 없이 접근 허용
            .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // H2 데이터베이스 콘솔 접근도 인증 없이 허용
            .anyRequest().authenticated() // 위의 조건을 제외한 모든 요청은 인증이 필요함
        );

    return http.build(); // HttpSecurity 객체를 사용하여 SecurityFilterChain 객체를 생성하고 반환
}
```
기본적으로 폼 로그인을 사용하며, 로그인에 성공하면 /home으로 실패하면 /login으로 리다이렉트 하는 심플한 구성이다.

그럼 이제 위에서 언급한대로 UserDetailsService만 구현해보자.
제일 먼저 `User`엔티티를 생성하여 사용자 객체를 생성한다.
### UserEntity
```java
@Entity  
@Getter  
@Setter  
@ToString  
public class Users {  
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  
    private String username;  
    private String email;  
    private String password;  
}
```

그 후, 인증에 사용될 `UserDetails` 를 구현한다.
### CustomUserDetails
```java
public class CustomUserDetails implements UserDetails {  
  
    Long id;  
    String username;  
    String email;  
    String password;  
  
    public CustomUserDetails(Users users) {  
        this.id = users.getId();  
        this.username = users.getUsername();  
        this.email = users.getEmail();  
        this.password = users.getPassword();  
    }  
  
    public CustomUserDetails(Long id, String username, String email, String password) {  
        this.id = id;  
        this.username = username;  
        this.email = email;  
        this.password = password;  
    }  
  
    @Override  
    public Collection<? extends GrantedAuthority> getAuthorities() {  
        return null;  
    }  
  
    @Override  
    public String getPassword() {  
        return password;  
    }  
  
    @Override  
    public String getUsername() {  
        return username;  
    }  
  
    @Override  
    public boolean isAccountNonExpired() {  
        return true;  
    }  
  
    @Override  
    public boolean isAccountNonLocked() {  
        return true;  
    }  
  
    @Override  
    public boolean isCredentialsNonExpired() {  
        return true;  
    }  
  
    @Override  
    public boolean isEnabled() {  
        return true;  
    }  
}
```

그리고 해당 인증 정보를 가지고 서비스를 만든다. 역시 `UserDetailsService`를 구현한다.
### UserDetailsService
```java
public class CustomUserDetailService implements UserDetailsService {  
  
    @Autowired  
    UsersRepository usersRepository;  
  
    @Override  
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  
        System.out.println("--------------------------------");  
        System.out.println("! LoadUserByUserName !");  
        System.out.println("--------------------------------");  
        // H2 데이터베이스에서 사용자를 조회한다.
        Users users = usersRepository.findByUsername(username);  
        System.out.println("--------------------------------");  
        System.out.println("! users : " + users.toString());  
        System.out.println("--------------------------------");  
        return new CustomUserDetails(users);  
    }  
}
```

### 화면 템플릿 구성

아까 언급한대로 home과 login 페이지를 구성한다.
로그인 페이지에서 폼의 액션은 실제 시큐리티 설정에서 로그인 URL로 지정한 경로와 동일하게 지정해야 한다.

#### home.html
```html
<!DOCTYPE html>  
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org">  
<head>  
    <title>Home</title>  
</head>  
<body>  
<h1>Home!</h1>  
<table>  
    <p>Welcome to home</p>  
</table>  
</body>  
</html>
```
#### login.html
```html
<!DOCTYPE html>  
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org">  
<head>  
    <title>Please Log In</title>  
</head>  
<body>  
<h1>Please Log In</h1>  
<div th:if="${param.error}">  
    Invalid username and password.</div>  
<div th:if="${param.logout}">  
    You have been logged out.</div>  
<form th:action="@{/loginProc}" method="post">  
    <div>  
        <input type="text" name="username" placeholder="Username"/>  
    </div>  
    <div>        <input type="password" name="password" placeholder="Password"/>  
    </div>  
    <input type="submit" value="Log in" />  
</form>  
</body>  
</html>
```

### 데이터 초기 구성
인메모리 H2 데이터베이스를 사용하기 때문에, 어플리케이션이 종료되면 데이터가 사라진다.
따라서, 구동 시에 데이터를 생성할 수 있도록 미리 스크립트를 짜서 넣어준다.
#### data.sql
```sql
DROP TABLE IF EXISTS users;  
  
CREATE TABLE users (  
                      id INT AUTO_INCREMENT PRIMARY KEY,  
                      username VARCHAR(255) NOT NULL,  
                      email VARCHAR(255) NOT NULL,  
                      password VARCHAR(255) NOT NULL  
);  
  
INSERT INTO users (username, email, password) VALUES ('user1', 'user1@example.com', 'pass1');  
INSERT INTO users (username, email, password) VALUES ('user2', 'user2@example.com', 'pass2');  
INSERT INTO users (username, email, password) VALUES ('user3', 'user3@example.com', 'pass3');  
INSERT INTO users (username, email, password) VALUES ('user4', 'user4@example.com', 'pass4');  
INSERT INTO users (username, email, password) VALUES ('user5', 'user5@example.com', 'pass5');  
INSERT INTO users (username, email, password) VALUES ('user6', 'user6@example.com', 'pass6');  
INSERT INTO users (username, email, password) VALUES ('user7', 'user7@example.com', 'pass7');  
INSERT INTO users (username, email, password) VALUES ('user8', 'user8@example.com', 'pass8');  
INSERT INTO users (username, email, password) VALUES ('user9', 'user9@example.com', 'pass9');  
INSERT INTO users (username, email, password) VALUES ('user10', 'user10@example.com', 'pass10');
```

## 테스트

### 로그인 페이지에서 로그인 시도

![Pasted image 20240407223508.png](src%2Fmain%2Fresources%2FPasted%20image%2020240407223508.png)

로그인 데이터 기준으로 로그인 시도

### 로그인 성공 확인

![Pasted image 20240407223536.png](src%2Fmain%2Fresources%2FPasted%20image%2020240407223536.png)

로그인에 성공함을 확인, 로그로 가보자!

### 로그 확인

![Pasted image 20240407223557.png](src%2Fmain%2Fresources%2FPasted%20image%2020240407223557.png)

시스템 로그에 내가 구현한 `UserDetailsService`의 `loadByUserName`이 호출되어 인증에 성공한 것을 확인할 수 있다.
