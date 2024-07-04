package com.cesar.authservice.client;

import com.cesar.authservice.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "US-SERVICE")
public interface UserServiceClient {
    @PostMapping("/api/users/find-by-email")
    User findByEmail(@RequestParam String email);

    @PutMapping("/api/users/save")
    ResponseEntity<?> saveUser(@RequestBody User user);
}
