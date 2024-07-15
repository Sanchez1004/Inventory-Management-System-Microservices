package com.cesar.usservice.repository;

import com.cesar.usservice.entity.SupplierEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository extends MongoRepository<SupplierEntity, String> {
}
