package com.cesar.usservice.repository;

import com.cesar.usservice.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    @Query("")
    List<UserEntity> findAllByRole(String role);

    Optional<UserEntity> findByEmail(String email);
}
