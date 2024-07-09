package com.cesar.authservice.client;

import com.cesar.authservice.entity.AuthUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "us-service")
public interface UserServiceClient {
    @PostMapping("/api/users/find-by-email")
    ResponseEntity<AuthUser> findByEmail(@RequestParam String email);

    @PutMapping("/api/users/save")
    ResponseEntity<Void> saveUser(@RequestBody AuthUser authUser);
}
