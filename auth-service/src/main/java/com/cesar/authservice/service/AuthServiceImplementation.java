package com.cesar.authservice.service;

import com.cesar.authservice.AuthException;
import com.cesar.authservice.dto.AuthResponse;
import com.cesar.authservice.entity.AuthUser;
import com.cesar.authservice.dto.LoginRequest;
import com.cesar.authservice.dto.RegisterRequest;
import com.cesar.authservice.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUserRepository authUserRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new AuthException("Login cannot be null");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthException e) {
            throw new AuthException("Invalid credentials");
        }

        Optional<AuthUser> authUser = authUserRepository.findByEmail(loginRequest.getEmail());
        if (authUser.isPresent()) {
            String token = jwtService.getToken(authUser.get());
            return AuthResponse.builder()
                    .token(token)
                    .build();
        }
        throw new AuthException("Invalid email or password");
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        Optional<AuthUser> authUser = authUserRepository.findByEmail(registerRequest.getEmail());
        if (authUser.isPresent()) {
            throw new AuthException("Email already in use");
        }

        AuthUser createdUser = authUserRepository.save(AuthUser
                .builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role("USER").build());

        return AuthResponse.builder()
                .token(jwtService.getToken(createdUser))
                .build();
    }

    @Override
    public Boolean validateToken(String token) {
        String email = jwtService.getEmailFromToken(token);

        if (email != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            return jwtService.isTokenValid(token, userDetails);
        }
        throw new AuthException("Invalid token");
    }

    @Override
    public AuthUser findUserByEmail(String email) {
        Optional<AuthUser> user = authUserRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        }
        throw new AuthException("User does not exist");
    }


}

