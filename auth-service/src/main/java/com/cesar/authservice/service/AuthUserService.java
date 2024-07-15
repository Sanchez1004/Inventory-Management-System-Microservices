package com.cesar.authservice.service;

import com.cesar.authservice.entity.AuthUser;
import com.cesar.authservice.entity.AuthUserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUserService {
    UserDetailsService userDetailsService();
    boolean userExistsByEmail(String email);
    void save(AuthUser build);
    AuthUserDTO findUserByEmail(String email);
}
