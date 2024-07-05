package com.cesar.usservice.security;

import com.cesar.usservice.client.AuthServiceClient;
import com.cesar.usservice.model.UserEntity;
import com.cesar.usservice.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final AuthServiceClient authServiceClient;
    private final UserService userService;

    @Value("${API_TOKEN}")
    private String apiToken;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, AuthServiceClient authServiceClient, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.authServiceClient = authServiceClient;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);

        if (token == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        ResponseEntity<Void> authResponse = authServiceClient.validateToken("Bearer " + token, apiToken);

        if (authResponse.getStatusCode() == HttpStatus.OK) {
            ResponseEntity<String> emailResponse = authServiceClient.getEmailFromToken("Bearer " + token, apiToken);
            if (emailResponse.getStatusCode() == HttpStatus.OK) {
                String email = emailResponse.getBody();
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            }
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

    @Bean
    public UserDetailsService userDetailService() {
        return email -> {
            UserEntity user = userService.getUserByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("Email not found");
            }
            return user;
        };
    }
}
