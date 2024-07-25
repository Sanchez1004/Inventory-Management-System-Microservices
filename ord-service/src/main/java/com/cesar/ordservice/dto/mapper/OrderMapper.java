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
                .clientFirstName(orderEntity.getClientFirstName())
                .clientLastName(orderEntity.getClientLastName())
                .clientMail(orderEntity.getClientEmail())
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
                .clientFirstName(orderDTO.getClientFirstName())
                .clientLastName(orderDTO.getClientLastName())
                .clientEmail(orderDTO.getClientMail())
                .date(orderDTO.getDate())
                .orderTotal(orderDTO.getOrderTotal())
                .orderStatus(orderDTO.getOrderStatus())
                .itemList(orderDTO.getItemList())
                .build();
    }
}
