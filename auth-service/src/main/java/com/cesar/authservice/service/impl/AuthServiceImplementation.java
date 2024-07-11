package com.cesar.authservice.service.impl;

import com.cesar.authservice.AuthException;
import com.cesar.authservice.dto.AuthResponse;
import com.cesar.authservice.dto.RegisterRequest;
import com.cesar.authservice.entity.AuthUser;
import com.cesar.authservice.entity.Role;

import com.cesar.authservice.service.AuthService;
import com.cesar.authservice.service.AuthUserService;
import com.cesar.authservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final AuthUserService authUserService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if (registerRequest.getEmail() != null && registerRequest.getPassword() != null) {
            authUserService.save(AuthUser.builder()
                            .email(registerRequest.getEmail())
                            .password(passwordEncoder.encode(registerRequest.getPassword()))
                            .firstName(registerRequest.getFirstName())
                            .lastName(registerRequest.getLastName())
                            .role(Role.USER)
                            .build());

            UserDetails userDetails = authUserService.userDetailsService().loadUserByUsername(registerRequest.getEmail());

            return AuthResponse.builder()
                    .token(jwtService.createToken(userDetails))
                    .build();
        }
        throw new AuthException("There was an error creating the user");
    }
}
