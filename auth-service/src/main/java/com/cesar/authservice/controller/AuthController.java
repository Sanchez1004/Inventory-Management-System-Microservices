package com.cesar.authservice.controller;

import com.cesar.authservice.exception.AuthException;
import com.cesar.authservice.dto.AuthResponse;
import com.cesar.authservice.dto.LoginRequest;
import com.cesar.authservice.dto.RegisterRequest;
import com.cesar.authservice.entity.AuthUserDTO;
import com.cesar.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${spring.security.secret.api.key}")
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

    @GetMapping("get-service-token")
    ResponseEntity<String> getServiceToken(@RequestParam String email, @RequestParam String password) {
        try {
            return ResponseEntity.ok(authService.getServiceToken(email, password));
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    @GetMapping("/extract-email")
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

    @GetMapping("get-user-by-email")
    ResponseEntity<AuthUserDTO> getUserByEmail(@RequestParam String email, @RequestParam String apiKey) {
        if (!apiKey.equals(defaultApiKey)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be empty");
        }
        try {
            return ResponseEntity.ok(authService.getUserByEmail(email));
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}

