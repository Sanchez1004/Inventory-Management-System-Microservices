package com.cesar.usservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @GetMapping("/api/auth/validate-token")
    ResponseEntity<Boolean> validateToken(@RequestParam("token") String token,  @RequestParam("apiKey") String apiKey);

    @GetMapping("/api/auth/get-email")
    ResponseEntity<String> getEmailFromToken(@RequestParam("token") String token, @RequestParam("apiKey") String apiKey);

    @GetMapping("/api/auth/get-auth")
    Authentication getAuthentication(String token);
}
