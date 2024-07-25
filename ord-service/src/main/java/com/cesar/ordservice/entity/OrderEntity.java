package com.cesar.ordservice.entity;

import com.cesar.ordservice.utils.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderEntity {
    @MongoId
    private String id;
    private OrderStatus orderStatus;
    private String clientId;
    private String clientFirstName;
    private String clientLastName;
    private String clientEmail;
    private Map<String, Integer> itemList;
    private double orderTotal;
    private LocalDateTime date;
}
