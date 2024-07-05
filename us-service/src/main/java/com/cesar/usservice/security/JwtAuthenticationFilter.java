package com.cesar.usservice.security;

import com.cesar.usservice.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);

        if (token == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        ResponseEntity<Void> authResponse = authServiceClient.validateToken("Bearer " + token);

        if (authResponse.getStatusCode() == HttpStatus.OK) {
            SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(token, null, null));
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final int authIndex = 7;

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(authIndex);
        }

        return null;
    }
}
