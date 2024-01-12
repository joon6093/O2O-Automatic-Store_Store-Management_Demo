package com.SJY.O2O_Automatic_Store_System_Demo.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        extractToken(request).map(token -> userDetailsService.loadUserByUsername(token)).ifPresent(userDetails -> setAuthentication(userDetails));
        chain.doFilter(request, response);
    }

    private void setAuthentication(CustomUserDetails userDetails) {
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

    private Optional<String> extractToken(ServletRequest request) {
        return Optional.ofNullable(((HttpServletRequest) request).getHeader("Authorization"));
    }
}