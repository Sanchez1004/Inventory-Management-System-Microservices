package com.cesar.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
