package com.cesar.usservice.config;

import com.cesar.usservice.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final AuthUserRepository authUserRepository;

    public UserDetailsService userDetailsService() {
        return username -> authUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void a () {
        userDetailsService().loadUserByUsername("ce").getAuthorities();
    }
}
