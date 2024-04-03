package dev.chiptune.springboot.config;

import dev.chiptune.springboot.config.filter.JwtAuthenticationEntryPoint;
import dev.chiptune.springboot.config.filter.JwtAuthenticationFilter;
import dev.chiptune.springboot.config.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        // h2 콘솔 경로 미적용
                        .ignoringAntMatchers("/h2-console/**")
                        // 토큰 발급 URL은 CSRF 미적용
                        .ignoringAntMatchers("/getToken")
                        .ignoringAntMatchers("/refreshAccessToken")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                // h2 사용을 위한 헤더 설정
                .headers().frameOptions().disable()
                .and()
                .cors().disable()
                .formLogin().disable() // 폼 로그인 비활성화
                .httpBasic(Customizer.withDefaults()) // Http 요청에 대한 설정
                .authorizeHttpRequests(authz -> authz
                        // 토큰 발급 구간은 인증 체크 안함.
                        .requestMatchers(new AntPathRequestMatcher("/getToken")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/refreshAccessToken")).permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling() // 에러 핸들링 정의
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // 인증 과정에서의 Exception Handling 클래스
                .and()
                // JWT Token 인증 필터를 Http Basic 요청 처리 필터인 BasicAuthenticationFilter 전으로 등록.
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), BasicAuthenticationFilter.class)
                .build();
    }

    // 비밀번호 인코딩을 위해 NoOpPasswordEncoder를 사용하는 PasswordEncoder 빈을 등록
    // 경고: 실제 환경에서는 사용하지 마세요!
    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
