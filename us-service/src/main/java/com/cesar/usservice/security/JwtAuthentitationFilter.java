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

/**
 * Filter that validates JWT tokens on every request and sets up the security context if the token is valid.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthentitationFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;
    private final CustomUserDetailsService userDetailsService;

    @Value("${spring.security.api.key}")
    private String apiKey;

    /**
     * This method intercepts HTTP requests and validates the JWT token provided in the Authorization header.
     * If the token is valid, it sets up the security context with the user's details.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs during filtering
     * @throws IOException      if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String userEmail;
        final int beginIndex = 7; // "Bearer " is 7 characters long

        // If the Authorization header is missing or doesn't start with "Bearer ", skip the filter
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            logger.info("Authorization header is missing or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT from the Authorization header
        jwt = authHeader.substring(beginIndex);
        logger.info("JWT extracted: " + jwt);

        try {
            // Extract the user's email from the JWT using the authentication service
            userEmail = authServiceClient.getEmailFromToken(jwt, apiKey).getBody();
            logger.info("Email from token: " + userEmail);

            // Proceed if the email is extracted successfully and the user is not already authenticated
            if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Validate the JWT using the authentication service
                if (Boolean.TRUE.equals(authServiceClient.validateToken(jwt, apiKey).getBody())) {
                    logger.info("Token is valid");

                    // Load user details using the email
                    UserDetails userDetails = userDetailsService.userDetailsService().loadUserByUsername(userEmail);
                    logger.info("User details loaded: " + userDetails.getUsername());
                    logger.info("User role is: " + userDetails.getAuthorities());

                    // Create an authentication token and set it in the security context
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

        // Proceed with the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
