package com.cesar.ordservice.controller;

import com.cesar.ordservice.dto.OrderDTO;
import com.cesar.ordservice.exception.OrderException;
import com.cesar.ordservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/get-order-by-id")
    ResponseEntity<OrderDTO> getOrderById(@RequestParam String orderId) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(orderId));
        } catch (OrderException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/create-order")
    ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            return ResponseEntity.ok(orderService.createOrder(orderDTO));
        } catch (OrderException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/update-order")
    ResponseEntity<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO) {
        try {
            return ResponseEntity.ok(orderService.updateOrder(orderDTO));
        } catch (OrderException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/delete-order-by-id")
    ResponseEntity<String> deleteOrderById(@RequestParam String orderId) {
        try {
            return ResponseEntity.ok(orderService.deleteOrderById(orderId));
        } catch (OrderException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
