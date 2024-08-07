package com.cesar.invservice.service;

import com.cesar.invservice.dto.InventoryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface InventoryService {
    List<InventoryDTO> getInventory();
    InventoryDTO getItemByName(String name);
    InventoryDTO getItemById(String id);
    InventoryDTO addItem(InventoryDTO inventoryDTO);
    InventoryDTO updateItemById(InventoryDTO inventoryDTO, String id);
    InventoryDTO updateItemByName(InventoryDTO inventoryDTO, String name) throws JsonProcessingException;
    Boolean addStockToItemById(String id, int quantity);
    Map<String, Integer> deductItemsById(Map<String, Integer> itemsForDeduct);
    boolean isItemAvailable(String itemId, Integer quantity);
    Boolean deductItemById(String itemId, Integer quantityForDeduct);
    Double getItemListTotal(Map<String, Integer>itemList);
    String deleteItemById(String id);
    String deleteItemByName(String name);
}
