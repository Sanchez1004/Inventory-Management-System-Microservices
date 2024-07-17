package com.cesar.usservice.repository;

import com.cesar.usservice.dto.ClientDTO;
import com.cesar.usservice.entity.ClientEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends MongoRepository<ClientEntity, String> {
    Optional<ClientEntity> findClientEntityById(String id);
    ClientEntity findByIdAndEmailAndFirstName(String id, String email, String firstName);
}
