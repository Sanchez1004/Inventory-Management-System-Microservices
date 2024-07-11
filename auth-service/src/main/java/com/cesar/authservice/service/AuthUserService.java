package com.cesar.authservice.service;

import com.cesar.authservice.entity.AuthUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUserService {
    UserDetailsService userDetailsService();
    void save(AuthUser build);
}
