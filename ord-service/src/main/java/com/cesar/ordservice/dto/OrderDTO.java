package com.cesar.ordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderDTO {
    @MongoId
    private String id;
    private String orderStatus;
    private String clientName;
    private String clientMail;
    private Map<String, Integer> itemList;
    private double orderTotal;
    private Date date;
}
