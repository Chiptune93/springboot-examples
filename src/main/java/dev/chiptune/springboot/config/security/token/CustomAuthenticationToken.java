package dev.chiptune.springboot.config.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String email;
    private final String credentials;

    // 인증 요청용 토큰, 인증 여부가 false
    public CustomAuthenticationToken(String email, String credentials) {
        super(email, credentials);
        this.email = email;
        this.credentials = credentials;
    }

    // 인증 성공용 토큰, 인증여부가 true
    public CustomAuthenticationToken(String email, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(email, credentials, null);
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
