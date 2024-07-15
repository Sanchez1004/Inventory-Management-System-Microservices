package com.cesar.usservice.repository;

import com.cesar.usservice.entity.ClientEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<ClientEntity, String> { }
