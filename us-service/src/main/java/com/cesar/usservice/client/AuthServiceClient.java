package com.cesar.usservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthServiceClient {
    @GetMapping("/api/auth/validate")
    ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String token);

    @GetMapping("/api/auth/get-email")
    ResponseEntity<String> getEmailFromToken(@RequestHeader("Authorization") String token);
}
