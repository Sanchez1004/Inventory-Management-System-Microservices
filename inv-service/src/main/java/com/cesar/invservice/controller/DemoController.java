package com.cesar.invservice.controller;

import com.cesar.invservice.dto.InventoryDTO;
import com.cesar.invservice.publisher.RabbitMQProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventories/v1")
public class DemoController {

    private final RabbitMQProducer rabbitMQProducer;

    public DemoController(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        rabbitMQProducer.sendStringMessage(message);
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }

    @GetMapping("/publish-json")
    public ResponseEntity<String> sendJsonMessage(@RequestBody InventoryDTO inventoryDTO) throws JsonProcessingException {
        rabbitMQProducer.sendJSONMessage(inventoryDTO);
        return ResponseEntity.ok("Object sent to RabbitMQ");
    }
}
