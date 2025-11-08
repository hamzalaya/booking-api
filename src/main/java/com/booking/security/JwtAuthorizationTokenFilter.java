package com.booking.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static com.booking.security.JwtTokenUtil.getUsernameFromToken;


@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationTokenFilter.class);
    @Value("${account.login.jwt.header}")
    private String tokenHeader;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public JwtAuthorizationTokenFilter(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException {

        try {
            LOGGER.debug("Processing authentication for '{}'", request.getRequestURL());
            JwtTokenUtil.getRequestJwtToken().ifPresent(token -> {
                setSecurityContext(request, getUsernameFromToken(token));
            });
            chain.doFilter(request, response);

        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            jwtAuthenticationEntryPoint.commence(request, response, authenticationException);
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
            jwtAuthenticationEntryPoint.commence(request, response, exception);
        }

    }

    private void setSecurityContext(HttpServletRequest httpServletRequest, String username) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        LOGGER.debug("Setting user {} in  security context", username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
