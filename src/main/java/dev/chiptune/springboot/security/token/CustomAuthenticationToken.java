package dev.chiptune.springboot.security.token;

import dev.chiptune.springboot.security.userDetails.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private Long id;
    private String userId;
    private String userPw;
    private String userType;
    private String userName;
    private boolean isSuccessAuthentication = false;


    public CustomAuthenticationToken(String userId, String userPw) {
        super(userId, userPw);
        this.userId = userId;
        this.userPw = userPw;
    }

    public CustomAuthenticationToken(CustomUserDetails user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getUserPw(), authorities);
        this.id = user.getId();
        this.userId = user.getUserId();
        this.userPw = user.getUserPw();
        this.userType = user.getUserType();
        this.userName = user.getUsername();
        this.isSuccessAuthentication = true;
    }

    @Override
    public String toString() {
        return "CustomAuthenticationToken{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userPw='" + userPw + '\'' +
                ", userType='" + userType + '\'' +
                ", userName='" + userName + '\'' +
                ", isSuccessAuthentication=" + isSuccessAuthentication +
                ", authorities=" + super.getAuthorities() +
                '}';
    }

    @Override
    public String getCredentials() {
        return this.userPw;
    }

    @Override
    public String getPrincipal() {
        return this.userId;
    }



}
