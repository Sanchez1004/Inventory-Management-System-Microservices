package com.cesar.authservice.service.impl;

import com.cesar.authservice.AuthException;
import com.cesar.authservice.entity.AuthUser;
import com.cesar.authservice.repository.AuthUserRepository;
import com.cesar.authservice.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImplementation implements AuthUserService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> authUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public boolean userExistsByEmail(String email) {
        Optional<AuthUser> authUser = authUserRepository.findByEmail(email);
        return authUser.isPresent();
    }

    public UserDetails getUserDetailsByEmail(String email) {
        return userDetailsService().loadUserByUsername(email);
    }

    @Override
    public void save(AuthUser build) {
        authUserRepository.save(build);
    }

    @Override
    public AuthUser findUserByEmail(String email) {
        Optional<AuthUser> user = authUserRepository.findByEmail(email);
        return user.orElseThrow(() -> new AuthException("User not found"));
    }
}
