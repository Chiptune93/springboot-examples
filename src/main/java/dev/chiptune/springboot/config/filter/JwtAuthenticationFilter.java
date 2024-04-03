package dev.chiptune.springboot.config.filter;

import dev.chiptune.springboot.config.token.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 로깅을 위한 SLF4J Logger 인스턴스를 생성합니다.
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // 생성자를 통해 JwtTokenProvider 의존성 주입
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 필터링 로직을 구현합니다. 모든 요청에 대해 이 메소드가 호출됩니다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("━━━━━━━━━━━━━━━━━JwtAuthenticationFilter START━☂☄");
        // 요청에서 JWT 토큰을 추출합니다.
        String token = jwtTokenProvider.extractToken(request);
        logger.info("Extracted Token : {}", token);

        // 토큰이 null이 아니라면 유효성 검사를 수행합니다.
        if (token != null) {
            try {
                if (jwtTokenProvider.validateAccessToken(token)) { // 토큰이 유효한 경우
                    // 토큰으로부터 사용자 정보를 추출합니다.
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    String password = jwtTokenProvider.getPasswordFromToken(token);

                    logger.info("from Token Info username  : {} ", username);
                    logger.info("from Token Info password  : {} ", password);


                    // 인증 정보를 만듭니다. 여기서는 권한(authorities) 리스트를 비워둡니다.
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, null);

                    // SecurityContext에 인증 정보를 설정합니다.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("☆ﾟ.*･｡ﾟ 토큰 유효성 검사를 통과하였습니다! ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
                } else { // 액세스 토큰이 유효하지 않은 경우
                    // 유효하지 않은 토큰인 경우 401 Unauthorized 응답 설정
                    throw new Exception("유효하지 않은 토큰입니다.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("━━━━━━━━━━━━━━━━━JwtAuthenticationFilter END━☂☄");
        // 현재 필터 이후에 정의된 필터들에 대한 처리를 계속 진행합니다.
        chain.doFilter(request, response);
    }
}
