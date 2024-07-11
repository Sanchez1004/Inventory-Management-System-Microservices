package com.cesar.authservice.service.impl;

import com.cesar.authservice.entity.AuthUser;
import com.cesar.authservice.repository.AuthUserRepository;
import com.cesar.authservice.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImplementation implements AuthUserService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return authUserRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public void save(AuthUser build) {
        authUserRepository.save(build);
    }
}
