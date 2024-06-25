package dev.chiptune.springboot.security.provider;

import dev.chiptune.springboot.repo.UserRepository;
import dev.chiptune.springboot.security.token.CustomAuthenticationToken;
import dev.chiptune.springboot.security.userDetails.CustomUserDetails;
import dev.chiptune.springboot.security.userDetails.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    public CustomProvider(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;

        System.out.println("------------------------------");
        System.out.println("customAuthenticationToken : " + customAuthenticationToken);
        System.out.println("------------------------------");


        CustomUserDetails user = new CustomUserDetails(customUserDetailsService.attempLogin(
                customAuthenticationToken.getPrincipal(),
                customAuthenticationToken.getCredentials()
        ));

        System.out.println("------------------------------");
        System.out.println("Provider user : " + user);
        System.out.println("------------------------------");

        if (user.getUsername() == null) {
            throw new BadCredentialsException("username is not found. username=" + user.getUsername());
        }
        // 인증 완료? 권한을 넣어서 토큰을 리턴해주자.
        user.setRoles(customUserDetailsService.getUserAuthorities(user.getUserId()));
        return new CustomAuthenticationToken(user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
