package com.cesar.usservice.client;

import com.cesar.usservice.security.AuthUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @GetMapping("/api/auth/validate-token")
    ResponseEntity<Boolean> validateToken(@RequestParam("token") String token, @RequestParam("apiKey") String apiKey);

    @GetMapping("/api/auth/extract-email")
    ResponseEntity<String> getEmailFromToken(@RequestParam("token") String token, @RequestParam("apiKey") String apiKey);

    @GetMapping("/api/auth/get-user-by-email")
    ResponseEntity<AuthUserDTO> getUserByEmail(@RequestParam String email, @RequestParam String apiKey);
}