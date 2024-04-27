package dev.chiptune.springboot.config;

import dev.chiptune.springboot.config.security.filter.CustomAuthenticationFilter;
import dev.chiptune.springboot.config.security.handler.CustomAccessDeniedHandler;
import dev.chiptune.springboot.config.security.handler.CustomAuthenticationEntryPoint;
import dev.chiptune.springboot.config.security.handler.CustomAuthenticationFailureHandler;
import dev.chiptune.springboot.config.security.handler.CustomAuthenticationSuccessHandler;
import dev.chiptune.springboot.config.security.provider.CustomAuthenticationProvider;
import dev.chiptune.springboot.config.security.userDetails.CustomUserDetailService;
import dev.chiptune.springboot.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final UsersService usersService;
    private final ObjectPostProcessor<Object> objectPostProcessor;


    /**
     * 시큐리티 설정 적용을 제외할 경로를 설정합니다.
     *
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**");
            }
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF (Cross-Site Request Forgery) 보호를 구성합니다.
                .csrf(csrf -> csrf
                        .ignoringAntMatchers("/h2-console/**", "/error/**") // "/h2-console/**" 경로에 대한 CSRF 보호를 비활성화합니다.
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
                        // .failureHandler(new CustomAuthenticationFailureHandler())
                        .loginProcessingUrl("/loginProc") // 로그인 폼이 제출될 URL을 설정합니다.
                        .permitAll() // 로그인 페이지에는 누구나 접근할 수 있도록 허용합니다.
                )
                // 로그아웃 기능을 활성화합니다.
                .logout()
                .and()
                // HTTP 요청에 대한 접근 제어를 구성합니다.
                .authorizeHttpRequests(authz -> authz
                        .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                        .antMatchers("/user/**").hasAnyAuthority("USER")
                        .requestMatchers(new AntPathRequestMatcher("/error/**")).permitAll() // "/error" 경로에 대해서는 인증 없이 접근을 허용합니다.
                        .requestMatchers(new AntPathRequestMatcher("/sample")).permitAll() // "/sample" 경로에 대해서는 인증 없이 접근을 허용합니다.
                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll() // "/login" 경로에 대해서도 인증 없이 접근을 허용합니다.
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // "/h2-console/**" 경로에 대해서도 인증 없이 접근을 허용합니다.
                        .anyRequest().authenticated() // 위에 정의된 경로를 제외한 모든 요청에 대해서는 인증을 요구합니다.
                )
                // 에러 핸들링을 구성한다.
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
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
        filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler()); // 인증 성공 핸들러를 설정합니다.
        // filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error")); // 인증 실패 핸들러를 설정합니다.
        filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler()); // ControllerAdvice에서 처리하기 위한 핸들러 등록
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
