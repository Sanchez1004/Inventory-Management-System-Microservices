package com.cesar.invservice.service;

import com.cesar.invservice.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {
    List<InventoryDTO> getInventory();
    InventoryDTO findItemByName(String name);
    InventoryDTO addItem(InventoryDTO inventoryDTO);
}
