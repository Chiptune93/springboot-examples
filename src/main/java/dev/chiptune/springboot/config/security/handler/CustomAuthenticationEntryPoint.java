package dev.chiptune.springboot.config.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 주석 부분은 단순히 에러 응답만 보낸다.
        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "커스텀 에러 핸들링! 인증이 필요합니다!");
        response.sendRedirect("/error/login-required");
    }
}
