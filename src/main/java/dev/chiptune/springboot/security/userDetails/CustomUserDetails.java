package dev.chiptune.springboot.security.userDetails;

import dev.chiptune.springboot.entity.User;
import dev.chiptune.springboot.security.authority.UserAuthorities;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String userId;
    private String userPw;
    private String userType;
    private String userName;
    private String userEmail;

    private UserAuthorities roles;


    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.userPw = user.getUserPw();
        this.userType = user.getUserType();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authoritiesCollection = new ArrayList<>();
        if (roles != null) {
            for (String roleName : roles.getUserTypeRole()) {
                // 역할명을 GrantedAuthority로 추가
                authoritiesCollection.add(new SimpleGrantedAuthority("ROLE_" + roleName));
            }
            for (String auth : roles.getAuthorities()) {
                authoritiesCollection.add(new SimpleGrantedAuthority(roles.getUserId() + "_" + auth));
            }
        }
        System.out.println("------------------------------");
        System.out.println("authoritiesCollection : " + authoritiesCollection);
        System.out.println("------------------------------");
        return authoritiesCollection;
    }

    @Override
    public String getPassword() {
        return userPw;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
