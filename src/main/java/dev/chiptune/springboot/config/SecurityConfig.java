package dev.chiptune.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
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
}
