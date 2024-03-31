package dev.chiptune.springboot.config.security.filter;

import dev.chiptune.springboot.config.security.token.CustomAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("username");
        String credentials = request.getParameter("password");

        return getAuthenticationManager().authenticate(new CustomAuthenticationToken(email, credentials, Collections.emptyList()));
    }
}
