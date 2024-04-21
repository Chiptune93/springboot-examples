package dev.chiptune.springboot.config.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String email;
    private final String credentials;

    // 인증 요청용 토큰, 인증 여부가 false
    public CustomAuthenticationToken(String email, String credentials) {
        // 인증 실패 시, 권한은 넘기지 않는다.
        super(email, credentials);
        this.email = email;
        this.credentials = credentials;
    }

    // 인증 성공용 토큰, 인증여부가 true
    public CustomAuthenticationToken(String email, String credentials, Collection<? extends GrantedAuthority> authorities) {
        // 인증 성공 시, 권한까지 같이 넘겨 토큰을 생성한다.
        super(email, credentials, authorities);
        this.email = email;
        this.credentials = credentials;
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
