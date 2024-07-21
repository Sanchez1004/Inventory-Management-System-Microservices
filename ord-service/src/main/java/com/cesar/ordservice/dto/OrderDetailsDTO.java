package com.cesar.ordservice.dto;

import com.cesar.ordservice.utils.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {
    private String id;
    private OrderStatus orderStatus;
    private double orderTotal;
}
 