package com.cesar.usservice.controller;

import com.cesar.usservice.dto.ClientDTO;
import com.cesar.usservice.dto.OrderDetailsDTO;
import com.cesar.usservice.exception.ClientException;
import com.cesar.usservice.service.ClientService;
import com.cesar.usservice.utils.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller for managing client-related operations.
 * This controller provides endpoints for retrieving, creating, updating, and deleting clients,
 * as well as managing client orders.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/clients")
public class ClientController {

    private final ClientService clientService;

    /**
     * Retrieves a client by their ID.
     *
     * @param id the ID of the client to retrieve
     * @return a ResponseEntity containing the ClientDTO if found
     */
    @GetMapping("/get-client-by-id")
    ResponseEntity<ClientDTO> getClientById(@RequestParam String id) {
        try {
            return ResponseEntity.ok(clientService.getClientById(id));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Finds clients based on a search keyword.
     *
     * @param keyword the keyword to search clients by
     * @return a ResponseEntity containing a list of ClientDTOs that match the keyword
     */
    @GetMapping("/get-client")
    ResponseEntity<List<ClientDTO>> findClientByKeyword(@RequestParam String keyword) {
        try {
            List<ClientDTO> clients = clientService.getListOfClientsByKeyword(keyword);
            if (clients.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No client found with keyword: " + keyword);
            }
            return ResponseEntity.ok(clients);
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Retrieves a client by their order ID.
     *
     * @param orderId the ID of the order associated with the client
     * @return a ResponseEntity containing the ClientDTO if found
     */
    @GetMapping("/get-client-by-order-id")
    ResponseEntity<ClientDTO> getClientByOrderId(@RequestParam String orderId) {
        try {
            ClientDTO client = clientService.getClientByOrderId(orderId);
            if (client == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "There is no client associated with the order id: " + orderId);
            }
            return ResponseEntity.ok(client);
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Saves a new client.
     *
     * @param clientDTO the client data to save
     * @return a ResponseEntity containing the saved ClientDTO
     */
    @PostMapping("/save-client")
    ResponseEntity<ClientDTO> saveClient(@RequestBody ClientDTO clientDTO) {
        try {
            return ResponseEntity.ok(clientService.saveClient(clientDTO));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Updates an existing client by their ID.
     *
     * @param clientDTO the updated client data
     * @param id        the ID of the client to update
     * @return a ResponseEntity containing the updated ClientDTO
     */
    @PutMapping("/update-client-by-id")
    ResponseEntity<ClientDTO> updateClientById(@RequestBody ClientDTO clientDTO, @RequestParam String id) {
        try {
            return ResponseEntity.ok(clientService.updateClientById(clientDTO, id));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Updates the orders of an existing client by their ID.
     *
     * @param orderDetailsDTO the order details to update
     * @param clientId        the ID of the client whose orders are being updated
     * @return a ResponseEntity containing the updated ClientDTO
     */
    @PutMapping("/update-client-orders-by-id")
    ResponseEntity<ClientDTO> updateClientOrdersById(@RequestBody OrderDetailsDTO orderDetailsDTO, @RequestParam String clientId) {
        try {
            return ResponseEntity.ok(clientService.updateClientOrdersByClientId(orderDetailsDTO, clientId));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Updates the status of a specific order for a client by their ID.
     *
     * @param orderId        the ID of the order to update
     * @param newOrderStatus the new status of the order
     * @param clientId       the ID of the client whose order status is being updated
     * @return a ResponseEntity containing the updated ClientDTO
     */
    @PutMapping("/update-client-order-status-by-client-id")
    ResponseEntity<ClientDTO> updateClientOrderStatusById(@RequestParam String orderId, @RequestParam OrderStatus newOrderStatus, @RequestParam String clientId) {
        try {
            return ResponseEntity.ok(clientService.updateClientOrderStatusByClientId(orderId, newOrderStatus, clientId));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Deletes an order by its ID for a specific client.
     *
     * @param clientId the ID of the client whose order is being deleted
     * @param orderId  the ID of the order to delete
     * @return a ResponseEntity containing a confirmation message
     */
    @PutMapping("/delete-order-by-id")
    ResponseEntity<String> deleteOrderById(@RequestParam String clientId, @RequestParam String orderId) {
        try {
            return ResponseEntity.ok(clientService.deleteOrderDetailsById(clientId, orderId));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Deletes a client by their ID.
     *
     * @param id the ID of the client to delete
     * @return a ResponseEntity containing a confirmation message
     */
    @DeleteMapping("/delete-client-by-id")
    ResponseEntity<String> deleteClientById(@RequestParam String id) {
        try {
            return ResponseEntity.ok(clientService.deleteClientById(id));
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Simple endpoint to test the controller.
     *
     * @return a greeting string "Hello"
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
