package com.cesar.usservice.dto.mapper;

import com.cesar.usservice.dto.ClientDTO;
import com.cesar.usservice.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public ClientEntity toEntity(ClientDTO clientDTO) {
        return ClientEntity.builder()
                .id(clientDTO.getId())
                .firstName(clientDTO.getLastName())
                .lastName(clientDTO.getLastName())
                .address(clientDTO.getAddress())
                .email(clientDTO.getEmail())
                .pendingOrders(clientDTO.getPendingOrders())
                .build();
    }

    public ClientDTO toDTO(ClientEntity clientEntity) {
        return ClientDTO.builder()
                .id(clientEntity.getId())
                .firstName(clientEntity.getLastName())
                .lastName(clientEntity.getLastName())
                .address(clientEntity.getAddress())
                .email(clientEntity.getEmail())
                .pendingOrders(clientEntity.getPendingOrders())
                .build();
    }
}
