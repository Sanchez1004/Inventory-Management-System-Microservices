package com.cesar.invservice.service.implementation;

import com.cesar.invservice.dto.InventoryDTO;
import com.cesar.invservice.dto.InventoryMapper;
import com.cesar.invservice.entity.InventoryEntity;
import com.cesar.invservice.exception.InventoryException;
import com.cesar.invservice.repository.InventoryRepository;
import com.cesar.invservice.service.InventoryService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LogManager.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public List<InventoryDTO> getInventory() {
        List<InventoryEntity> inventory = inventoryRepository.findAll();
        if (!inventory.isEmpty()) {
            return inventory.stream()
                    .map(inventoryMapper::toDTO)
                    .toList();
        }
        logger.error("There's no items in inventory");
        throw new InventoryException("No items in inventory!");
    }

    @Override
    public InventoryDTO findItemByName(String name) {
        return inventoryMapper.toDTO(inventoryRepository.findByItemName(name)
                .orElseThrow(() -> new InventoryException("Item not found with name" + name)));
    }

    @Override
    public InventoryDTO addItem(InventoryDTO inventoryDTO) {
    if (inventoryDTO.getItem().getName() != null && inventoryDTO.getItem().getPrice() >= 0 && inventoryDTO.getItem().getQuantity() >= 0) {
        InventoryEntity inventoryEntity = inventoryMapper.toEntity(findItemByName(inventoryDTO.getItem().getName()));
        if (inventoryEntity == null) {
            return inventoryMapper.toDTO(inventoryRepository.save(inventoryMapper.toEntity(inventoryDTO)));
        }
        logger.info("Item already exists with name: {}", inventoryDTO.getItem().getName());
        throw new InventoryException("Item already exists with name: " + inventoryDTO.getItem().getName());
        }
    logger.error("Fill item name field!");
    throw new InventoryException("Fill basic item field: name");
    }
}
