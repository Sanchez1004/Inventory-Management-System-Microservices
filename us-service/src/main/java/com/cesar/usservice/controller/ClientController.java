package com.cesar.usservice.controller;

import com.cesar.usservice.dto.ClientDTO;
import com.cesar.usservice.dto.OrderDetailsDTO;
import com.cesar.usservice.exception.ClientException;
import com.cesar.usservice.service.ClientService;
import com.cesar.usservice.utils.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/get-client-by-id")
    ResponseEntity<ClientDTO> getClientById(@RequestParam String id) {
        try {
            return ResponseEntity.ok(clientService.getClientById(id));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/get-client")
    ResponseEntity<List<ClientDTO>> findClientByKeyword(@RequestParam String keyword) {
            try {
            List<ClientDTO> clients = clientService.getListOfClientsByKeyword(keyword);
            if (clients.isEmpty()) { //Test conditions
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No client found with keyword: " + keyword);
            }
            return ResponseEntity.ok(clients);
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/get-client-by-order-id")
    ResponseEntity<ClientDTO> getClientByOrderId(@RequestParam String orderId) {
        try {
            ClientDTO client = clientService.getClientByOrderId(orderId);
            if (client == null) { //Test conditions
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "There is no client associated with the order id: " + orderId);
            }
            return ResponseEntity.ok(client);
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/save-client")
    ResponseEntity<ClientDTO> saveClient(@RequestBody ClientDTO clientDTO) {
        try {
            return ResponseEntity.ok(clientService.saveClient(clientDTO));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/update-client-by-id")
    ResponseEntity<ClientDTO> updateClientById(@RequestBody ClientDTO clientDTO, @RequestParam String id) {
        try {
            return ResponseEntity.ok(clientService.updateClientById(clientDTO, id));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/update-client-orders-by-id")
    ResponseEntity<ClientDTO> updateClientOrdersById(@RequestBody OrderDetailsDTO orderDetailsDTO, @RequestParam String clientId) {
        try {
            return ResponseEntity.ok(clientService.updateClientOrdersByClientId(orderDetailsDTO, clientId));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/update-client-order-status-by-client-id")
    ResponseEntity<ClientDTO> updateClientOrderStatusById(@RequestParam String orderId, @RequestParam OrderStatus newOrderStatus, @RequestParam String clientId) {
        try {
            return ResponseEntity.ok(clientService.updateClientOrderStatusByClientId(orderId, newOrderStatus, clientId));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/delete-order-by-id")
    ResponseEntity<String> deleteOrderById(@RequestParam String clientId, @RequestParam String orderId) {
        try {
            return ResponseEntity.ok(clientService.deleteOrderDetailsById(clientId, orderId));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/delete-client-by-id")
    ResponseEntity<String> deleteClientById(@RequestParam String id) {
        try {
            return ResponseEntity.ok(clientService.deleteClientById(id));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
