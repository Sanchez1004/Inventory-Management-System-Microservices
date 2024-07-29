package com.cesar.notservice.consumer;

import com.cesar.notservice.dto.InventoryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private final ObjectMapper objectMapper;

    public RabbitMQConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = {"${rabbitmq.inventory.queue.string.name}"})
    public void consumeInventoryStringQueue(String message) {
        logger.info("Received message -> {}", message);
    }

    @RabbitListener(queues = {"${rabbitmq.inventory.queue.json.name}"})
    public void consumeInventoryJSONQueue(String jsonMessage) throws JsonProcessingException {
        InventoryDTO inventoryDTO = objectMapper.readValue(jsonMessage, InventoryDTO.class);
        logger.info("Received object -> {}", inventoryDTO);
    }
}
