package com.cesar.invservice.service.implementation;

import com.cesar.invservice.dto.InventoryDTO;
import com.cesar.invservice.dto.InventoryMapper;
import com.cesar.invservice.entity.InventoryEntity;
import com.cesar.invservice.exception.InventoryException;
import com.cesar.invservice.repository.InventoryRepository;
import com.cesar.invservice.service.InventoryService;
import com.cesar.invservice.utils.InventoryField;
import com.cesar.invservice.utils.Item;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LogManager.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final Map<InventoryField, BiConsumer<InventoryEntity, InventoryDTO>> updateFieldMap = new EnumMap<>(InventoryField.class);


    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;

        initializeUpdateFieldMap();
    }

    private void initializeUpdateFieldMap() {
        updateFieldMap.put(InventoryField.ITEM_NAME, (entity, request) -> {
            if (!request.getItem().getName().isEmpty()) {
                entity.getItem().setName(request.getItem().getName());
            }
        });
        updateFieldMap.put(InventoryField.ITEM_DESCRIPTION, (entity, request) -> {
            if (!request.getItem().getDescription().isEmpty()) {
                entity.getItem().setDescription(request.getItem().getDescription());
            }
        });
        updateFieldMap.put(InventoryField.ITEM_CATEGORY, (entity, request) -> {
            if (request.getItem().getCategory() != null) {
                entity.getItem().setCategory(request.getItem().getCategory());
            }
        });
        updateFieldMap.put(InventoryField.ITEM_PRICE, (entity, request) -> {
            if (request.getItem().getPrice() >= 0) {
                entity.getItem().setPrice(request.getItem().getPrice());
            }
        });
        updateFieldMap.put(InventoryField.ITEM_QUANTITY, (entity, request) -> {
            if (request.getItem().getQuantity() >= 0) {
                entity.getItem().setQuantity(request.getItem().getQuantity());
            }
        });
        updateFieldMap.put(InventoryField.SALE_PRICE, (entity, request) -> {
            if (request.getSalePrice() >= 0) {
                entity.setSalePrice(request.getSalePrice());
            }
        });
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
    public InventoryDTO getItemByName(String name) {
        return inventoryMapper.toDTO(inventoryRepository.findByItemName(name)
                .orElseThrow(() -> new InventoryException("Item not found with name" + name)));
    }

    @Override
    public InventoryDTO getItemById(String id) {
        return inventoryMapper.toDTO(inventoryRepository.findInventoryEntityById(id)
                .orElseThrow(() -> new InventoryException("Item not found with id" + id)));
    }

    @Override
    public InventoryDTO addItem(InventoryDTO inventoryDTO) {
        if (inventoryDTO.getItem().getName() != null && inventoryDTO.getItem().getPrice() >= 0 && inventoryDTO.getItem().getQuantity() >= 0) {
            InventoryEntity inventoryEntity = inventoryMapper.toEntity(getItemByName(inventoryDTO.getItem().getName()));
            if (inventoryEntity == null) {
                return inventoryMapper.toDTO(inventoryRepository.save(inventoryMapper.toEntity(inventoryDTO)));
            }
            logger.info("Item already exists with name: {}", inventoryDTO.getItem().getName());
            throw new InventoryException("Item already exists with name: " + inventoryDTO.getItem().getName());
        }
        logger.error("Fill item name field!");
        throw new InventoryException("Fill basic item field: name");
    }

    @Override
    public InventoryDTO updateItemById(InventoryDTO inventoryDTO, String id) {
        if (id.isEmpty() && inventoryDTO.getItem() == null) {
            logger.error("Item id cannot be empty and at least one item field has to be modified");
            throw new InventoryException("Item id cannot be empty and at least one item field has to be modified");
        }
        InventoryEntity inventoryEntity = inventoryMapper.toEntity(getItemById(id));
        if (inventoryEntity != null) {
            for (Map.Entry<InventoryField, BiConsumer<InventoryEntity, InventoryDTO>> entry : updateFieldMap.entrySet()) {
                entry.getValue().accept(inventoryEntity, inventoryDTO);
            }

            return inventoryMapper.toDTO(inventoryRepository.save(inventoryEntity));
        }
        logger.error("The item with id {} doesn't exist or the request has a bad format", id);
        throw new InventoryException("There was an error updating the item");
    }

    @Override
    public InventoryDTO updateItemByName(InventoryDTO inventoryDTO, String name) {
        if (name.isEmpty() && inventoryDTO.getItem() == null) {
            logger.error("Item name cannot be empty and at least one item field has to be modified");
            throw new InventoryException("Item name cannot be empty and at least one item field has to be modified");
        }
        InventoryEntity inventoryEntity = inventoryMapper.toEntity(getItemByName(name));
        if (inventoryEntity != null) {
            for (Map.Entry<InventoryField, BiConsumer<InventoryEntity, InventoryDTO>> entry : updateFieldMap.entrySet()) {
                entry.getValue().accept(inventoryEntity, inventoryDTO);
            }

            return inventoryMapper.toDTO(inventoryRepository.save(inventoryEntity));
        }
        logger.error("The item with {} doesn't exist or the request has a bad format", name);
        throw new InventoryException("There was an error updating the item");
    }

    @Override
    public InventoryDTO addStockToItemById(String id, int quantity) {
        if (quantity > 0) {
            InventoryEntity inventoryEntity = inventoryMapper.toEntity(getItemById(id));
            if (inventoryEntity != null) {
                BiConsumer<InventoryEntity, InventoryDTO> updateOrderDetails = updateFieldMap.get(InventoryField.ITEM_QUANTITY);
                updateOrderDetails.accept(inventoryEntity, InventoryDTO.builder()
                                .item(Item.builder().quantity(quantity).build())
                                .build()
                );
                return inventoryMapper.toDTO(inventoryRepository.save(inventoryEntity));
            }
            logger.error("No item exists with id {}", id);
            throw new InventoryException("No item exists with id: " + id);
        }
        logger.error("Quantity has to be more than zero");
        throw new InventoryException("Quantity has to be more than zero");
    }

    @Transactional
    @Override
    public Map<String, Integer> deductItemsById(Map<String, Integer> itemsForDeduct) {
        Map<String, Integer> failedItems = new LinkedHashMap<>();
        if (!itemsForDeduct.isEmpty()) {
            for (Map.Entry<String, Integer> entry : itemsForDeduct.entrySet()) {
                String id = entry.getKey();
                int quantityForDeduct = entry.getValue();

                InventoryEntity inventoryEntity = inventoryMapper.toEntity(getItemById(id));
                if (inventoryEntity.getItem() != null && inventoryEntity.getItem().getQuantity() < quantityForDeduct) {
                    failedItems.put(inventoryEntity.getItem().getName(), inventoryEntity.getItem().getQuantity());
                }
                if (inventoryEntity.getItem() != null && failedItems.isEmpty()) {
                    inventoryEntity.getItem().setQuantity(inventoryEntity.getItem().getQuantity() + quantityForDeduct);
                    inventoryRepository.save(inventoryEntity);
                }
            }
            if (!failedItems.isEmpty()) {
                return failedItems;
            }
            return null;
        }
        logger.error("There are no items for process");
        throw new InventoryException("There are no items for process");
    }

    @Override
    public String deleteItemById(String id) {
        InventoryEntity inventoryEntity = inventoryMapper.toEntity(getItemById(id));
        if (inventoryEntity != null) {
            inventoryRepository.delete(inventoryEntity);
            return "Item deleted successfully with id: " + id;
        }
        return "There was an error deleting the item with id: - " + id + " -, check if there exist an item with that id!";
    }

    @Override
    public String deleteItemByName(String name) {
        InventoryEntity inventoryEntity = inventoryMapper.toEntity(getItemByName(name));
        if (inventoryEntity != null) {
            inventoryRepository.delete(inventoryEntity);
            return "Item deleted successfully with name: " + name;
        }
        return "There was an error deleting the item with name: - " + name + " -, check if there exist an item with that name!";    }
}
