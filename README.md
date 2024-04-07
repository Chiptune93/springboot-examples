## Form Login With Custom Authentication

[저번 포스트](https://chiptune93.github.io/backend/spring/spring-security-pratice2.html)에서 구현했던 간단한 폼 로그인 구현에 이어서 이번에는 커스텀
필터를 구현하는 폼 로그인을 구현하고자 한다.
기본적으로 폼 로그인 시 수행하는 `UsernamePasswordAuthenticationFilter`를 확장하여 구현한다.

### 어떤 기능을 추가할 것인가?

그렇다면 커스텀 필터를 구현함에 있어 어떤 기능을 추가할 것인가에 대한 문제가 있다.

#### loadUserByUsername 대신 사용자 정보 불러오기

기존 `loadUserByUsername` 메소드의 경우, `username`만을 사용하여 사용자 정보를 불러온다.
여기에 추가적으로 비밀번호까지 적용하여 아이디로만 데이터를 조회하는 것이 아니라 비밀번호까지 조건에 넣어가져오고자 한다.

```java

@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("--------------------------------");
    System.out.println("! LoadUserByUserName !");
    System.out.println("--------------------------------");
    Users users = usersRepository.findByUsername(username);
    System.out.println("--------------------------------");
    System.out.println("! users : " + users.toString());
    System.out.println("--------------------------------");
    return new CustomUserDetails(users);
}
```

하지만 오버라이드한 `loadUserByUsername` 메소드는 `username`만 파라미터로 가져올 수 있다. 즉, 해당 메소드로는 요구 조건을 충족하기 어려우니 다른 곳에서 해당 구간을 처리할 수 있어야
한다.

> `username` 파라미터는 실제 시큐리티 내에서 인증 정보 중, `principal`항목에 해당하는 변수명이다.
> 네임이라고 해서 꼭 유저명이 들어가는 것이 아닌 아이디 같은 항목이 들어가도 상관없다,
> 다만, 내부에서 사용하는 파라미터명이 username으로 고정될 뿐이다.

### 기본 플로우와 다르게 구성하려면 어떻게 해야할까?

커스텀 구간을 추가하려면 해당 구간을 구현한, 예를 들면 커스텀 필터만 추가하고 끝나는 것이 아니다.
기본 인증 매니저 구성은 다음으로 넘길 필터나 구간이 정해져있기 때문에 원하는 순서를 만드려면 실제로 그 순서를 따르게끔
인증 매니저와 공급자를 구성해주어야 한다.

다음은 이전에도 보았던 순서도 중 일부이다.
![Pasted image 20240407234317.png](src%2Fmain%2Fresources%2FPasted%20image%2020240407234317.png)
여기서 필터에서 토큰을 생성하고 인증 매니저에게 넘긴다.
인증매니저는 이후 아래와 같은 절차를 따른다.
![Pasted image 20240407234920.png](src%2Fmain%2Fresources%2FPasted%20image%2020240407234920.png)
위 이미지 처럼 우리가 만약 인증 처리를 커스텀하고 싶다면 다음과 같이 해야 한다.

1. **커스텀 인증을 위한 인증 공급자 클래스를 생성한다.**
2. **해당 공급자를 인증 매니저에 등록한다.**
3. **커스텀 필터에 해당 인증 매니저를 등록하여 요청 처리 시, 해당 인증 매니저를 사용하도록 한다.**

그렇다면 위 내용을 구현해보자!

### 커스텀 인증 공급자 구성

인증 공급자를 구성하기 위해서는 `AuthenticationProvider`를 구현하여야 한다.
인증 절차에는 처음에 말했듯, 패스워드까지 검증하는 구간이 추가되었다. 해당 코드는 따로 구현된 userService에 의해 호출되며 UserDetailsService를 호출하지 않는다.

```java

@Component
/**
 * 실제 인증을 담당하는 인증 공급자 클래스  
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsersService usersService;

    public CustomAuthenticationProvider(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        System.out.println("------------------------------");
        System.out.println("username in AuthenticationProvider -> " + username);
        System.out.println("password in AuthenticationProvider -> " + password);
        System.out.println("------------------------------");

        UserDetails user = new CustomUserDetails(usersService.findByUsernameAndPassword(username, password));

        if (user.getUsername() == null) {
            throw new BadCredentialsException("username is not found. username=" + username);
        }

        return new CustomAuthenticationToken(username, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

여기서 중요한 포인트는 다음과 같다.

#### authenticate

`authenticate`메소드는 필터에서 요청해오는 **"인증 요청"** 객체에 대해 실제 인증 처리를 해야 하는 메소드이며, 인증이 종료되면 **"인증 여부"** 가 결정된 **"인증 완료된"** 토큰 객체를
리턴해야 한다. 인증 완료된 객체는 실제 인증에 "성공" 또는 "실패" 처리가 되어야 한다.

#### supports

해당 메소드는 이 인증 공급자가 어떤 요청에 대해 처리를할 것인지 제한하는 메소드이다.
여기서는 `CustomAuthenticationToken` 객체가 인증 요청으로써 들어오게 되면 처리할 수 있게 하고 나머지 인증 요청에 대해서는 무시하겠다는 내용이다.
만약 이 제한이 없다면 여러 필터에서 거쳐오는 (또는 요청에 의해 시큐리티가 기본 생성하여 던지는) 요청 까지 처리 할 수 있어, 커스텀 인증 공급자를 구성할 때는 이를 제한하는 것이 낫다.

### 인증 토큰 구성하기

우리는 현재 `UsernamePasswordAuthentication`에 대해 인증 구성을 하고 있기 때문에 토큰 또한 `UsernamePasswordAuthenticationToken`을 확장하여 구현한다.

토큰의 기본 클래스인 `AbstractAuthenticationToken`를 확장해서 구현해도 상관 없으나, 인증 여부 세팅 구간에서 별도의 세팅을 해주어야 한다.
실제 `UsernamePasswordAuthentication`에서는 생성자를 통한 생성 시에도 값에 따라 인증 여부를 true 또는 false로 구현할 수
있으나 `AbstractAuthenticationToken`에서는 생성자를 통해서가 아닌 별도로 인증 여부를 세팅해주어야 한다.

```java
// UsernamePasswordAuthenticationToken 클래스 내부 생성자 설정
public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
    super((Collection) null);
    this.principal = principal;
    this.credentials = credentials;
    this.setAuthenticated(false); // 인증 실패
}

public UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(true);  // 인증 성공
}
```

따라서, 이에 맞추어 커스텀 토큰에서도 동일하게 설정해주도록 한다.
여기서는 super()를 통해 인증 성공 여부를 설정하고 있다.

```java
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String email;
    private final String credentials;

    // 인증 요청용 토큰, 인증 여부가 false    public CustomAuthenticationToken(String email, String credentials) {  
        super(email,credentials);  
        this.email =email;  
        this.credentials =credentials;
}

// 인증 성공용 토큰, 인증여부가 true    public CustomAuthenticationToken(String email, String credentials, Collection<? extends GrantedAuthority> authorities) {  
        super(email,credentials,null);
        this.email =email;  
        this.credentials =credentials;  
    }

@Override
public Object getCredentials() {
    return this.credentials;
}

@Override
public Object getPrincipal() {
    return this.email;
}  
}
```

### 커스텀 필터 구현하기

이제 위 공급자 및 토큰을 사용하는 커스텀 필터를 구현할 차례다.
`UsernamePasswordAuthenticationFilter`를 확장하여 구현한다. 그리하여 폼 로그인에서 오는 요청을 기본 필터인 `UsernamePasswordAuthenticationFilter` 대신
수행하도록 한다.

```java
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("username");
        String credentials = request.getParameter("password");

        return getAuthenticationManager().authenticate(new CustomAuthenticationToken(email, credentials, null));
    }
}
```

### 전체적인 수행 과정

![Pasted image 20240408001114.png](src%2Fmain%2Fresources%2FPasted%20image%2020240408001114.png)
위와 같은 흐름으로 인증을 처리한다.
여기서 주목할 점은 실제 우리가 구현한 커스텀 인증 공급자에서 인증 완료 처리를 하더라도 다음 필터로 진행된다는 것이다.
그렇다면 인증을 또 시도하는 것인가? 그렇지 않다.

#### 시큐리티는 인증이 완료된 객체에 대해서는 필터 수행을 하지 않는다.

시큐리티 필터들은 전 단계 필터에서 넘어온 인증 객체 혹은 SecurityContextHolder 에서 조회한 인증 객체가 "인증" 된 상태인 경우 인증 필터 수행을 하지 않고 다음으로 넘긴다.

따라서, 커스텀 인증 공급자에서 인증 완료가 되는 경우 이후 필터에 대해 수행하지 않는다. (인증 목적 필터만, 인증 목적이 아닌 다른 필터는 그대로 실행됨!)

그래서 만약, 커스텀 인증 공급자에서 인증 실패가 되는 경우, 인증되지 않은 객체가 그대로 넘어가기 때문에 이후 필터가 실행될 가능성이 있다. 그러므로 인증 실패의 경우 바로 Exception 이나 에러 처리를
하는게 좋다.

### Security Config 등록

#### CustomAuthenticationFilter - 커스텀 인증 필터 빈 등록

설정에서 새 커스텀 필터 빈 등록을 합니다.

```java

@Bean
public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
    CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager()); // 인증 관리자를 설정합니다.  
    filter.setFilterProcessesUrl("/loginProc"); // 인증 처리 URL을 설정합니다.  
    filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/home")); // 인증 성공 핸들러를 설정합니다.  
    filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error")); // 인증 실패 핸들러를 설정합니다.  
    return filter; // CustomAuthenticationFilter 인스턴스를 반환합니다.  
}
```

#### CustomAuthenticationProvider - 커스텀 인증 공급자 등록

마찬가지로 커스텀 인증 공급자를 등록합니다.

```java

@Bean
public AuthenticationProvider authenticationProvider() {
    return new CustomAuthenticationProvider(usersService);
}
```

#### AuthenticationManager - 기본 인증 매니저에 해당 공급자 추가

시큐리티 기본 인증 매니저를 가져와 커스텀 인증 공급자를 추가한 후, 빌드하도록 합니다.

```java

@Bean
public AuthenticationManager authenticationManager() throws Exception {
    AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
    builder.authenticationProvider(authenticationProvider()); // CustomAuthenticationProvider를 인증 제공자로 추가합니다.
    return builder.build(); // AuthenticationManager 객체를 생성하여 반환합니다.
}

```

#### 전체 설정 내용

```java

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final UsersService usersService;
    private final ObjectPostProcessor<Object> objectPostProcessor;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF (Cross-Site Request Forgery) 보호를 구성합니다.  
                .csrf(csrf -> csrf
                        .ignoringAntMatchers("/h2-console/**") // "/h2-console/**" 경로에 대한 CSRF 보호를 비활성화합니다.  
                )
                // CORS (Cross-Origin Resource Sharing) 설정을 비활성화합니다.  
                .cors().disable()
                // HTTP 기본 인증을 비활성화합니다.  
                .httpBasic().disable()
                // 헤더의 "X-Frame-Options"을 비활성화하여, H2 콘솔과 같은 리소스를 <iframe> 내에서 사용할 수 있게 합니다.  
                .headers().frameOptions().disable()
                .and()
                // 폼 로그인을 구성합니다.  
                .formLogin(form -> form
                        .loginPage("/login") // 사용자 정의 로그인 페이지 URL을 설정합니다.  
                        .defaultSuccessUrl("/home") // 로그인 성공 시 리다이렉션될 기본 URL을 설정합니다.  
                        .failureUrl("/login") // 로그인 실패 시 리다이렉션될 URL을 설정합니다.  
                        .loginProcessingUrl("/loginProc") // 로그인 폼이 제출될 URL을 설정합니다.  
                        .permitAll() // 로그인 페이지에는 누구나 접근할 수 있도록 허용합니다.  
                )
                // 로그아웃 기능을 활성화합니다.  
                .logout()
                .and()
                // HTTP 요청에 대한 접근 제어를 구성합니다.  
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(new AntPathRequestMatcher("/sample")).permitAll() // "/sample" 경로에 대해서는 인증 없이 접근을 허용합니다.  
                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll() // "/login" 경로에 대해서도 인증 없이 접근을 허용합니다.  
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // "/h2-console/**" 경로에 대해서도 인증 없이 접근을 허용합니다.  
                        .anyRequest().authenticated() // 위에 정의된 경로를 제외한 모든 요청에 대해서는 인증을 요구합니다.  
                )
                // 커스텀 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가합니다.  
                .addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build(); // SecurityFilterChain 객체를 생성하여 반환합니다.  
    }


    @Bean
    CustomUserDetailService customUserDetailService() {
        return new CustomUserDetailService();
    }

    // 비밀번호 인코딩을 위해 NoOpPasswordEncoder를 사용하는 PasswordEncoder 빈을 등록  
    // 경고: 실제 환경에서는 사용하지 마세요!  
    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager()); // 인증 관리자를 설정합니다.  
        filter.setFilterProcessesUrl("/loginProc"); // 인증 처리 URL을 설정합니다.  
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/home")); // 인증 성공 핸들러를 설정합니다.  
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error")); // 인증 실패 핸들러를 설정합니다.  
        return filter; // CustomAuthenticationFilter 인스턴스를 반환합니다.  
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(usersService);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        builder.authenticationProvider(authenticationProvider()); // CustomAuthenticationProvider를 인증 제공자로 추가합니다.  
        return builder.build(); // AuthenticationManager 객체를 생성하여 반환합니다.  
    }


}
```

### 테스트

#### 로그인 시도

![Pasted image 20240408002211.png](src%2Fmain%2Fresources%2FPasted%20image%2020240408002211.png)
로그인 시도 후, 성공했다!
그럼 과연 시스템에서는 기존 `UserDetailsService`를 호출하지 않고
커스텀 필터를 통해 인증 했는지를 확인한다.

![Pasted image 20240408002226.png](src%2Fmain%2Fresources%2FPasted%20image%2020240408002226.png)

#### 시스템 로그

![Pasted image 20240408002308.png](src%2Fmain%2Fresources%2FPasted%20image%2020240408002308.png)
시스템 로그를 확인해본 결과 실제 필터를 통과하여 새로 생성한 커스텀 인증 공급자에서
인증 처리를 진행한 것을 확인할 수 있었다!
