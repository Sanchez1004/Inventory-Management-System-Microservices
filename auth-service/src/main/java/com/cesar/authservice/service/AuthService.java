package com.cesar.authservice.service;

import com.cesar.authservice.dto.AuthResponse;
import com.cesar.authservice.dto.LoginRequest;
import com.cesar.authservice.dto.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    String getEmailFromToken(String token);
    UserDetails getUserDetailsByEmail(String email);
    Boolean validateToken(String token, UserDetails userDetails);
    public UserDetailsService getUserDetailsService();
}
