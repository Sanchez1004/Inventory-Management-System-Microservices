package com.cesar.usservice.repository;

import com.cesar.usservice.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    @Query("")
    List<UserEntity> findAllByRole(String role);
}
