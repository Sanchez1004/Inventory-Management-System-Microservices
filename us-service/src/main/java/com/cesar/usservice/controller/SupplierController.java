package com.cesar.usservice.controller;

import com.cesar.usservice.dto.SupplierDTO;
import com.cesar.usservice.exception.SupplierException;
import com.cesar.usservice.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for managing supplier-related operations.
 * This controller provides endpoints for retrieving, creating, updating, and deleting suppliers.
 */
@RestController
@RequiredArgsConstructor  // Automatically injects final fields via constructor injection.
@RequestMapping("/api/users/suppliers")  // Base path for all supplier-related endpoints.
public class SupplierController {

    private final SupplierService supplierService;  // Service layer dependency for handling business logic.

    /**
     * Retrieves a supplier by their ID.
     *
     * @param id the ID of the supplier to retrieve
     * @return a ResponseEntity containing the SupplierDTO if found
     */
    @GetMapping("/get-supplier-by-id")
    ResponseEntity<SupplierDTO> getSupplierById(@RequestParam String id) {
        try {
            return ResponseEntity.ok(supplierService.getSupplierById(id));
        } catch (SupplierException e) {
            // Converts the SupplierException to a HTTP 400 Bad Request response.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Saves a new supplier.
     *
     * @param supplierDTO the supplier data to save
     * @return a ResponseEntity containing the saved SupplierDTO
     */
    @PostMapping("/save-supplier")
    ResponseEntity<SupplierDTO> saveSupplier(@RequestBody SupplierDTO supplierDTO) {
        try {
            return ResponseEntity.ok(supplierService.saveSupplier(supplierDTO));
        } catch (SupplierException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Updates an existing supplier by their ID.
     *
     * @param supplierDTO the updated supplier data
     * @param id          the ID of the supplier to update
     * @return a ResponseEntity containing the updated SupplierDTO
     */
    @PutMapping("/update-supplier-by-id")
    ResponseEntity<SupplierDTO> updateSupplierById(@RequestBody SupplierDTO supplierDTO, @RequestParam String id) {
        try {
            return ResponseEntity.ok(supplierService.updateSupplierById(supplierDTO, id));
        } catch (SupplierException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Deletes a supplier by their ID.
     *
     * @param id the ID of the supplier to delete
     * @return a ResponseEntity containing a confirmation message
     */
    @DeleteMapping("/delete-supplier-by-id")
    ResponseEntity<String> deleteSupplierById(@RequestParam String id) {
        try {
            return ResponseEntity.ok(supplierService.deleteSupplierById(id));
        } catch (SupplierException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
