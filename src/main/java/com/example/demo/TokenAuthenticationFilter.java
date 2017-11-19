package com.example.demo;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class TokenAuthenticationFilter extends GenericFilterBean {


    private Collection<? extends GrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority("Test-Role"),
            new SimpleGrantedAuthority("Test-Role2")
    );

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        //extract token from header
        final String accessToken = httpRequest.getHeader("accessToken");
        if (null != accessToken) {
            //get and check whether token is valid ( from DB or file wherever you are storing the token)

            //Populate SecurityContextHolder by fetching relevant information using token
            final User user = new User(
                    "username",
                    "password",
                    true,
                    true,
                    true,
                    true,
                    authorities);
            final UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        chain.doFilter(request, response);
    }

}
