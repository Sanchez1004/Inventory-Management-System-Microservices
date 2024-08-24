package com.cesar.usservice.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * Feign client interface for interacting with the order service.
 * This client is used to communicate with the "ord-service", which handles order-related operations.
 * As it stands, this interface is empty, so it currently doesn't define any methods for interaction with the order service.
 */
@FeignClient(name = "ord-service")
public interface OrderServiceClient {
    // No methods are defined yet.
}
