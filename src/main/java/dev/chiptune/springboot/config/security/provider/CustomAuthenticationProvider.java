package dev.chiptune.springboot.config.security.provider;

import dev.chiptune.springboot.config.security.token.CustomAuthenticationToken;
import dev.chiptune.springboot.config.security.userDetails.CustomUserDetails;
import dev.chiptune.springboot.service.UsersService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
/**
 * 실제 인증을 담당하는 인증 공급자 클래스
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsersService usersService;

    public CustomAuthenticationProvider(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        System.out.println("------------------------------");
        System.out.println("username in AuthenticationProvider -> " + username);
        System.out.println("password in AuthenticationProvider -> " + password);
        System.out.println("------------------------------");

        UserDetails user = new CustomUserDetails(usersService.findByUsernameAndPassword(username, password));

        if (user.getUsername() == null) {
            throw new BadCredentialsException("username is not found. username=" + username);
        }

        return new CustomAuthenticationToken(username, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
