package com.cesar.authservice.controller;

import com.cesar.authservice.AuthException;
import com.cesar.authservice.dto.AuthResponse;
import com.cesar.authservice.dto.LoginRequest;
import com.cesar.authservice.dto.RegisterRequest;
import com.cesar.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${API_KEY}")
    String defaultApiKey;

    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        try {
            return ResponseEntity.ok(authService.register(registerRequest));
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authService.login(loginRequest));
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    @GetMapping("/get-email")
    ResponseEntity<String> getEmailFromToken(@RequestParam("token") String token, @RequestParam("apiKey") String apiKey) {
        if (!apiKey.equals(defaultApiKey)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            return ResponseEntity.ok(authService.getEmailFromToken(token));
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/validate-token")
    ResponseEntity<Boolean> validateToken(@RequestParam("token") String token,
                                          @RequestParam("apiKey") String apiKey) {
        if (!apiKey.equals(defaultApiKey)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            return ResponseEntity.ok(authService.validateToken(token));
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/get-auth")
    Authentication getAuthentication(String token) {
        return authService.getAuthentication(token);
    }
}

