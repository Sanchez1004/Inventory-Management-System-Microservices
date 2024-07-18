package com.cesar.invservice.service;

import com.cesar.invservice.dto.InventoryDTO;

import java.util.List;
import java.util.Map;

public interface InventoryService {
    List<InventoryDTO> getInventory();
    InventoryDTO getItemByName(String name);
    InventoryDTO getItemById(String id);
    InventoryDTO addItem(InventoryDTO inventoryDTO);
    InventoryDTO updateItemById(InventoryDTO inventoryDTO, String id);
    InventoryDTO updateItemByName(InventoryDTO inventoryDTO, String name);
    InventoryDTO addStockToItemById(String id, int quantity);
    Map<String, Integer> deductItemsById(Map<String, Integer> itemsForDeduct);
    String deleteItemById(String id);
    String deleteItemByName(String name);
}
