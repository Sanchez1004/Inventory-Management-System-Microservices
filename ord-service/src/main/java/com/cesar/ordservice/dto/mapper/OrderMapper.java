package com.cesar.ordservice.dto.mapper;

import com.cesar.ordservice.dto.OrderDTO;
import com.cesar.ordservice.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderDTO toDTO(OrderEntity orderEntity) {
        return OrderDTO.builder()
                .id(orderEntity.getId())
                .clientId(orderEntity.getClientId())
                .clientName(orderEntity.getClientName())
                .clientMail(orderEntity.getClientMail())
                .date(orderEntity.getDate())
                .orderTotal(orderEntity.getOrderTotal())
                .orderStatus(orderEntity.getOrderStatus())
                .itemList(orderEntity.getItemList())
                .build();
    }

    public OrderEntity toEntity(OrderDTO orderDTO) {
        return OrderEntity.builder()
                .id(orderDTO.getId())
                .clientId(orderDTO.getClientId())
                .clientName(orderDTO.getClientName())
                .clientMail(orderDTO.getClientMail())
                .date(orderDTO.getDate())
                .orderTotal(orderDTO.getOrderTotal())
                .orderStatus(orderDTO.getOrderStatus())
                .itemList(orderDTO.getItemList())
                .build();
    }
}
