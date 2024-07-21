package com.cesar.ordservice.client;

import com.cesar.ordservice.dto.ClientDTO;
import com.cesar.ordservice.dto.OrderDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "us-service")
public interface UserServiceClient {
    // /api/users/clients

    @GetMapping("/api/users/clients/get-client-by-id")
    ResponseEntity<ClientDTO> getClientById(@RequestParam String id);

    @PutMapping("/api/users/clients/update-client-orders-by-id")
    ResponseEntity<ClientDTO> updateClientOrdersById(@RequestBody OrderDetailsDTO orderDetails, @RequestParam String clientId);
}
