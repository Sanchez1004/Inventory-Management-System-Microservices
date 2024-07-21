package com.cesar.ordservice.repository;

import com.cesar.ordservice.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    Optional<OrderEntity> findOrderEntityById(String id);
}
