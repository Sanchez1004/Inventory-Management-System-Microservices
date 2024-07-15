package com.cesar.authservice.service;

import com.cesar.authservice.entity.AuthUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AuthUserService {
    UserDetailsService userDetailsService();
    boolean userExistsByEmail(String email);
    void save(AuthUser build);
    AuthUser findUserByEmail(String email);
}
