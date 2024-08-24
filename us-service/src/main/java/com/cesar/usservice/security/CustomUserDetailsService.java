package com.cesar.usservice.security;

import com.cesar.usservice.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of the UserDetailsService interface for loading user-specific data.
 * This service interacts with an external authentication service to retrieve user details.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final AuthServiceClient authServiceClient;

    @Value("${API_KEY}")
    private String apiKey;

    /**
     * Returns a UserDetailsService that loads user-specific data from the authentication service.
     *
     * @return a UserDetailsService implementation that fetches user details by email.
     */
    public UserDetailsService userDetailsService() {
        /*
        Previous implementation that fetched user details directly
        from the authentication service using only the username (email).
        return username -> {
            AuthUserDTO authUser = authServiceClient.getUser(username);

            return new User(
                    authUser.getEmail(),
                    authUser.getPassword(),
                    List.of(new SimpleGrantedAuthority(authUser.getRole().name()))
            );
        };
        */

        // Improved implementation that retrieves the user details via the authentication service,
        // using the provided username (email) and an API key for authorization.
        return username -> authServiceClient.getUserByEmail(username, apiKey).getBody();
    }
}
