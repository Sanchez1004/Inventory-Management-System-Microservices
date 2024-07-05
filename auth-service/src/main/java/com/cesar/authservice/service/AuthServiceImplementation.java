package com.cesar.authservice.service;

import com.cesar.authservice.AuthException;
import com.cesar.authservice.client.UserServiceClient;
import com.cesar.authservice.dto.AuthResponse;
import com.cesar.authservice.dto.LoginRequest;
import com.cesar.authservice.dto.RegisterRequest;
import com.cesar.authservice.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthException e) {
            throw new AuthException("Invalid credentials");
        }

        User user = findByEmail(loginRequest.getEmail());
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role("USER")
                .build();

        save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    private User findByEmail(String email) {
        return userServiceClient.findByEmail(email);
    }

    private void save(User user) {
        userServiceClient.saveUser(user);
    }
}

