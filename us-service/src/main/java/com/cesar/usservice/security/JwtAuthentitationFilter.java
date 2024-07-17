package com.cesar.usservice.security;

import com.cesar.usservice.client.AuthServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthentitationFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;
    private final CustomUserDetailsService userDetailsService;

    @Value("${API_KEY}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String userEmail;
        final int beginIndex = 7;

        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            logger.info("Authorization header is missing or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(beginIndex);
        logger.info("JWT extracted: " + jwt);

        try {
            userEmail = authServiceClient.getEmailFromToken(jwt, apiKey).getBody();
            logger.info("Email from token: " + userEmail);

            if (StringUtils.isNotEmpty(userEmail)  && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (Boolean.TRUE.equals(authServiceClient.validateToken(jwt, apiKey).getBody())) {
                    logger.info("Token is valid");

                    UserDetails userDetails = userDetailsService.userDetailsService().loadUserByUsername(userEmail);
                    logger.info("User details loaded: " + userDetails.getUsername());
                    logger.info("User role is: " + userDetails.getAuthorities());

                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(token);
                    SecurityContextHolder.setContext(securityContext);
                } else {
                    logger.info("Token is not valid");
                }
            } else {
                logger.info("User email is empty or authentication is already set");
            }
        } catch (Exception e) {
            logger.error("Error during JWT validation", e);
        }

        filterChain.doFilter(request, response);
    }
}
