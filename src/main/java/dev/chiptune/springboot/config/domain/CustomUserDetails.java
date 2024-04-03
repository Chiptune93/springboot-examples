package dev.chiptune.springboot.config.domain;

import dev.chiptune.springboot.entity.Users;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomUserDetails implements UserDetails {

    // Getter와 Setter
    private String username;
    private String password;
    private String accessToken;
    private String refreshToken;

    public CustomUserDetails(Users users) {
        this.username = users.getUsername();
        this.password = users.getPassword();
    }

    // UserDetails 인터페이스 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 권한 관련 처리 필요
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO 계정 만료 체크
        // = 계정에 사용기한 있는 경우에만 구현.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO 계정이 잠겨있는 지 여부 체크
        // = 계정 잠금 기능이 있는 경우 구현
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO 구현 필요: 패스워드 만료 여부 체크
        // = 패스워드 설정 기한이 존재하는 경우 구현
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO 구현 필요: 사용 가능한 활성화 계정 여부
        // = 사용 가능 여부 체크 경우만 구현
        return true;
    }

}

