package com.cesar.invservice.repository;

import com.cesar.invservice.entity.InventoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<InventoryEntity, String> {
    Optional<InventoryEntity> findInventoryEntityById(String id);
    Optional<InventoryEntity> findByItemName(String name);
}
