package com.cesar.authservice.service;

import com.cesar.authservice.dto.AuthResponse;
import com.cesar.authservice.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
}
