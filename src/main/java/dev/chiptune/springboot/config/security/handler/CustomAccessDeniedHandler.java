package dev.chiptune.springboot.config.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 주석 부분은 단순히 에러 응답만 보낸다.
        // response.sendError(HttpServletResponse.SC_FORBIDDEN, "커스텀 액세스 핸들링! 권한이 없습니다!");
        response.sendRedirect("/error/access-denied");
    }
}

