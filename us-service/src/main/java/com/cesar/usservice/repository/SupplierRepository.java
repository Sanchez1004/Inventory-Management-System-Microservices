package com.cesar.usservice.repository;

import com.cesar.usservice.entity.SupplierEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SupplierRepository extends MongoRepository<SupplierEntity, String> {
    Optional<SupplierEntity> findSupplierEntityById(String id);
    SupplierEntity findSupplierEntityByIdAndNameAndEmail(String id, String name, String email);
}
