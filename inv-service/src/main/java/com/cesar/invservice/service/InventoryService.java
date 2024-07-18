package com.cesar.invservice.service;

import com.cesar.invservice.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {
    List<InventoryDTO> getInventory();
    InventoryDTO getItemByName(String name);
    InventoryDTO getItemById(String id);
    InventoryDTO addItem(InventoryDTO inventoryDTO);
    InventoryDTO updateItemById(InventoryDTO inventoryDTO, String id);
    InventoryDTO updateItemByName(InventoryDTO inventoryDTO, String name);
    String deleteItemById(String id);
    String deleteItemByName(String name);
}
