package com.cesar.invservice.controller;

import com.cesar.invservice.dto.InventoryDTO;
import com.cesar.invservice.exception.InventoryException;
import com.cesar.invservice.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Map;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/get-inventory")
    ResponseEntity<List<InventoryDTO>> getInventory() {
        try {
            return ResponseEntity.ok(inventoryService.getInventory());
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/get-item-by-name")
    ResponseEntity<InventoryDTO> getItemByName(@RequestParam String name) {
        try {
            return ResponseEntity.ok(inventoryService.getItemByName(name));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/get-item-by-id")
    ResponseEntity<InventoryDTO> getItemById(@RequestParam String id) {
        try {
            return ResponseEntity.ok(inventoryService.getItemById(id));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/check-item-availability")
    ResponseEntity<Boolean> isItemAvailable(@RequestParam String itemId, @RequestParam Integer quantity) {
        try {
            return ResponseEntity.ok(inventoryService.isItemAvailable(itemId, quantity));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/get-item-list-total")
    ResponseEntity<Double> getItemListTotal(@RequestBody Map<String, Integer> itemList) {
        try {
            return ResponseEntity.ok(inventoryService.getItemListTotal(itemList));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/create-item")
    ResponseEntity<InventoryDTO> addItem(@RequestBody InventoryDTO inventoryDTO) {
        try {
            return ResponseEntity.ok(inventoryService.addItem(inventoryDTO));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/update-item-by-id")
    ResponseEntity<InventoryDTO> updateItemById(@RequestBody InventoryDTO inventoryDTO,@RequestParam String id) {
        try {
            return ResponseEntity.ok(inventoryService.updateItemById(inventoryDTO, id));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/update-item-by-name")
    ResponseEntity<InventoryDTO> updateItemByName(@RequestBody InventoryDTO inventoryDTO, @RequestParam String name) {
        try {
            return ResponseEntity.ok(inventoryService.updateItemByName(inventoryDTO, name));
        } catch (InventoryException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/add-stock-by-id")
    ResponseEntity<Boolean> addStockToItem(@RequestParam String id, @RequestParam int quantity) {
        try {
            return ResponseEntity.ok(inventoryService.addStockToItemById(id, quantity));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/deduct-items-by-id")
    ResponseEntity<Map<String, Integer>> deductItemsById(@RequestBody Map<String, Integer> itemsForDeduct) {
        try {
            return ResponseEntity.ok(inventoryService.deductItemsById(itemsForDeduct));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/deduct-item-by-id")
    ResponseEntity<Boolean> deductItemById(@RequestParam String itemId, @RequestParam Integer quantityForDeduct) {
        try {
            return ResponseEntity.ok(inventoryService.deductItemById(itemId, quantityForDeduct));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/delete-item-by-id")
    ResponseEntity<String> deleteItemById(@RequestParam String id) {
        try {
            return ResponseEntity.ok(inventoryService.deleteItemById(id));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/delete-item-by-name")
    ResponseEntity<String> deleteItemByName(@RequestParam String name) {
        try {
            return ResponseEntity.ok(inventoryService.deleteItemByName(name));
        } catch (InventoryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
