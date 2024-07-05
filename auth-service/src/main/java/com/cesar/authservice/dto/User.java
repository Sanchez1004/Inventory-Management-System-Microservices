package com.cesar.authservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

    public String getRoleWithPrefix() {
        return "ROLE_" + this.role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRoleWithPrefix()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
