package com.cesar.invservice.security;

import com.cesar.invservice.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final AuthServiceClient authServiceClient;

    @Value("${spring.security.api.key}")
    private String apiKey;

    public UserDetailsService userDetailsService() {
        // this one worked with /api/auth/get-us (param -> email)
//        return username -> {
//            AuthUserDTO authUser = authServiceClient.getUser(username);
//
//            return new User(
//                    authUser.getEmail(),
//                    authUser.getPassword(),
//                    List.of(new SimpleGrantedAuthority(authUser.getRole().name()))
//            );
//        };
        return username -> authServiceClient.getUserByEmail(username, apiKey).getBody(); // This is a better option
    }
}
