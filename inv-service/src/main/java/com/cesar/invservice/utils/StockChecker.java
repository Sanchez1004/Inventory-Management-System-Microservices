package com.cesar.invservice.utils;

import com.cesar.invservice.dto.InventoryDTO;
import com.cesar.invservice.publisher.RabbitMQProducer;
import com.cesar.invservice.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockChecker {

    private static final Logger logger = LoggerFactory.getLogger(StockChecker.class);

    private final InventoryService inventoryService;
    private final RabbitMQProducer rabbitMQProducer;

    public StockChecker(InventoryService inventoryService, RabbitMQProducer rabbitMQProducer) {
        this.inventoryService = inventoryService;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    // Configure the fixed rate to make this method execute more often as you need
    @Scheduled(fixedRate = 1000000)
    public void checkStockLevels() throws JsonProcessingException {
        List<InventoryDTO> items = inventoryService.getInventory();
        for (InventoryDTO item : items) {
            if (item.getItem().getQuantity() <= item.getThreshold()) {
                logger.info("Item below threshold with id: '{}' & name: '{}'", item.getId(), item.getItem().getName());
                item.setHandleCategory(HandleCategory.LOW_STOCK);
                notifyLowStock(item);
            }
        }
    }

    private void notifyLowStock(InventoryDTO item) throws JsonProcessingException {
        rabbitMQProducer.sendJSONMessage(item);
    }
}
