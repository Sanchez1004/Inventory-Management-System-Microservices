package com.cesar.usservice.controller;

import com.cesar.usservice.dto.SupplierDTO;
import com.cesar.usservice.exception.SupplierException;
import com.cesar.usservice.service.SupplierService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/get-supplier-by-id")
    ResponseEntity<SupplierDTO> getSupplierById(@RequestParam String id) {
        try {
            return ResponseEntity.ok(supplierService.getSupplierById(id));
        } catch (SupplierException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/save-supplier")
    ResponseEntity<SupplierDTO> saveSupplier(@RequestBody SupplierDTO supplierDTO) {
        try {
            return ResponseEntity.ok(supplierService.saveSupplier(supplierDTO));
        } catch (SupplierException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/update-supplier-by-id")
    ResponseEntity<SupplierDTO> updateSupplierById(@RequestBody SupplierDTO supplierDTO, @RequestParam String id) {
        try {
            return ResponseEntity.ok(supplierService.updateSupplierById(supplierDTO, id));
        } catch (SupplierException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

   @DeleteMapping("/delete-supplier-by-id")
   ResponseEntity<String> deleteSupplierById(@RequestParam String id) {
       try {
           return ResponseEntity.ok(supplierService.deleteSupplierById(id));
       } catch (SupplierException e) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
       }
   }
}
