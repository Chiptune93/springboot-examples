package dev.chiptune.springboot.config.security.userDetails;

import dev.chiptune.springboot.entity.AuthType;
import dev.chiptune.springboot.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    Long id;
    String username;
    String email;
    String password;
    List<AuthType> auth = new ArrayList<>();

    public CustomUserDetails(Users users) {
        System.out.println(users.toString());
        this.id = users.getId();
        this.username = users.getUsername();
        this.email = users.getEmail();
        this.password = users.getPassword();
        if (users.getAuthType() == AuthType.ALL) {
            this.auth.add(AuthType.USER);
            this.auth.add(AuthType.ADMIN);
        } else {
            this.auth.add(users.getAuthType());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return auth.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
