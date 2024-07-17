package com.cesar.usservice.dto.mapper;

import com.cesar.usservice.dto.SupplierDTO;
import com.cesar.usservice.entity.SupplierEntity;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
    public SupplierEntity toEntity(SupplierDTO supplierDTO) {
        return SupplierEntity.builder()
                .id(supplierDTO.getId())
                .name(supplierDTO.getName())
                .email(supplierDTO.getEmail())
                .address(supplierDTO.getAddress())
                .maxQuota(supplierDTO.getMaxQuota())
                .minQuota(supplierDTO.getMinQuota())
                .build();
    }

    public SupplierDTO toDTO(SupplierEntity supplierEntity) {
        return SupplierDTO.builder()
                .id(supplierEntity.getId())
                .name(supplierEntity.getName())
                .email(supplierEntity.getEmail())
                .address(supplierEntity.getAddress())
                .maxQuota(supplierEntity.getMaxQuota())
                .minQuota(supplierEntity.getMinQuota())
                .build();
    }
}
