package com.cesar.ordservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "inv-service")
public interface InventoryServiceClient {

    // /api/inventories

    @PutMapping("/api/inventories/deduct-items-by-id")
    ResponseEntity<Map<String, Integer>> deductItemsById(Map<String, Integer> itemsForDeduct);

    @GetMapping("/api/inventories/get-item-list-total")
    ResponseEntity<Double> getItemListTotal(@RequestParam Map<String, Integer> itemList);
}
