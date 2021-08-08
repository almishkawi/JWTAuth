package com.mo.JWTAuth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationVerificationFilter extends BasicAuthenticationFilter {
    public JWTAuthenticationVerificationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(WebSecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");

        if (authToken != null && authToken.startsWith(WebSecurityConstants.TOKEN_PREFIX)) {

            String userEmail = JWT.require(Algorithm.HMAC512(WebSecurityConstants.SECRET_TOKEN.getBytes()))
                    .build().verify(authToken.replace(WebSecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();

            if (userEmail != null) {
                return new UsernamePasswordAuthenticationToken(userEmail, null, new ArrayList<>());
            }

            return null;
        }

        return null;
    }
}
