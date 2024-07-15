package com.cesar.authservice.repository;

import com.cesar.authservice.entity.AuthUser;
import com.cesar.authservice.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AuthUserRepository extends MongoRepository<AuthUser, String> {
    Optional<AuthUser> findByEmail(String email);
    List<AuthUser> findByRole(Role role);
}
