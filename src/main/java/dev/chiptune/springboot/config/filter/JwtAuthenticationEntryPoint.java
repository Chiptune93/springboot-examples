package dev.chiptune.springboot.config.filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final static Logger logger = Logger.getLogger(JwtAuthenticationEntryPoint.class.getName());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        sendAuthenticationError(response, authException.toString());
    }

    private void sendAuthenticationError(HttpServletResponse response, String message) throws IOException {
        logger.info("━━━━━━━━━━━━━━━━━JwtAuthenticationEntryPoint START━☂☄");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        logger.info("☆ﾟ.*･｡ﾟ JWT Token 인증 중, 에러 발생 > " + message + " ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
        response.getWriter().write("로그인 과정 중 문제가 발생하였습니다.");
        logger.info("━━━━━━━━━━━━━━━━━JwtAuthenticationEntryPoint END━☂☄");
    }
}


