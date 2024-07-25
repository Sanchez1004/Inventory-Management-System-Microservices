package com.cesar.ordservice.dto;

import com.cesar.ordservice.utils.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String id;
    private OrderStatus orderStatus;
    private String clientId;
    private String clientFirstName;
    private String clientLastName;
    private String clientMail;
    private Map<String, Integer> itemList;
    private double orderTotal;
    private LocalDateTime date;
}
