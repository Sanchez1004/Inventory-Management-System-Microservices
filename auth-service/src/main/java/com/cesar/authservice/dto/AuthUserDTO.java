package com.cesar.authservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
