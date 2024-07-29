package com.cesar.invservice.publisher;

import com.cesar.invservice.dto.InventoryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}") String exchangeName;
    @Value("${rabbitmq.inventory.routing.key.string.name}") String stringRoutingKey;
    @Value("${rabbitmq.inventory.routing.key.json.name}") String jsonRoutingKey;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendStringMessage(String message) {
        logger.info("Message sent -> {}", message);
        rabbitTemplate.convertAndSend(exchangeName, stringRoutingKey, message);
    }

    public void sendJSONMessage(InventoryDTO inventoryDTO) throws JsonProcessingException {
        logger.info("Object sent -> {}", inventoryDTO);
        String jsonObject = objectMapper.writeValueAsString(inventoryDTO);
        rabbitTemplate.convertAndSend(exchangeName, jsonRoutingKey, jsonObject);
    }
}
