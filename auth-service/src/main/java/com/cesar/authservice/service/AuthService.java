package com.cesar.authservice.service;

import com.cesar.authservice.dto.AuthResponse;
import com.cesar.authservice.dto.LoginRequest;
import com.cesar.authservice.dto.RegisterRequest;
import com.cesar.authservice.entity.AuthUserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    String getEmailFromToken(String token);
    Boolean validateToken(String token);
    AuthUserDTO getUserByEmail(String email);

    UserDetails getUserDetails(String email);
}
