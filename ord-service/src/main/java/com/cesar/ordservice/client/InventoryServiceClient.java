package com.cesar.ordservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "inv-service")
public interface InventoryServiceClient {

    // /api/inventories

    @PutMapping("/api/inventories/deduct-item-by-id")
    ResponseEntity<Boolean> deductItemById(@RequestParam String itemId, @RequestParam Integer quantityForDeduct);

    @PutMapping("/api/inventories/deduct-items-by-id")
    ResponseEntity<Map<String, Integer>> deductItemsById(@RequestBody Map<String, Integer> itemsForDeduct);

    @GetMapping("/api/inventories/get-item-list-total")
    ResponseEntity<Double> getItemListTotal(@RequestBody Map<String, Integer> itemList);

    @PutMapping("/api/inventories/add-stock-by-id")
    ResponseEntity<Boolean> addStockToItem(@RequestParam String id, @RequestParam int quantity);
}
