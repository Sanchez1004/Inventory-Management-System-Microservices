package com.cesar.usservice.service;

import com.cesar.usservice.dto.AuthResponse;
import com.cesar.usservice.dto.LoginRequest;
import com.cesar.usservice.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse register(RegisterRequest registerRequest);
}
