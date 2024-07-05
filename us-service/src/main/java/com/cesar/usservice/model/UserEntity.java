package com.cesar.usservice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @MongoId
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

    private String getRoleWithPrefix() {
        return ("ROLE_" + this.role);
    }
}
