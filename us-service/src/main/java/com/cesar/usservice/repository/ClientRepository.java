package com.cesar.usservice.repository;

import com.cesar.usservice.entity.ClientEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface    ClientRepository extends MongoRepository<ClientEntity, String> {

    Optional<ClientEntity> findClientEntityById(String id);

    @Query("{ '$or': [ " +
            "  { 'firstName': { $regex: ?0, $options: 'i' } }, " +
            "  { 'lastName': { $regex: ?0, $options: 'i' } }, " +
            "  { 'email': { $regex: ?0, $options: 'i' } }, " +
            "  { 'id': { $regex: ?0, $options: 'i' } }, " +
            "  { 'address.streetName': { $regex: ?0, $options: 'i' } }, " +
            "  { 'address.houseNumber': { $regex: ?0, $options: 'i' } }, " +
            "  { 'address.city': { $regex: ?0, $options: 'i' } }, " +
            "  { 'address.zipCode': { $regex: ?0, $options: 'i' } }, " +
            "  { 'address.neighborhood': { $regex: ?0, $options: 'i' } }, " +
            "  { 'address.state': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<ClientEntity> findClientsByKeyword (String keyword);

    @Query("{ 'pendingOrders.id': ?0 }")
    Optional<ClientEntity> findByOrderDetailsId(String orderId);
}
