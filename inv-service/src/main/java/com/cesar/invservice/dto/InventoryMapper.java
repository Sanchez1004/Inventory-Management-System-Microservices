package com.cesar.invservice.dto;

import com.cesar.invservice.entity.InventoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class InventoryMapper {
    public InventoryDTO toDTO(InventoryEntity inventoryEntity) {
        return InventoryDTO.builder()
                .item(inventoryEntity.getItem())
                .salePrice(inventoryEntity.getSalePrice())
                .build();
    }

    public InventoryEntity toEntity(InventoryDTO inventoryDTO) {
        return InventoryEntity.builder()
                .item(inventoryDTO.getItem())
                .salePrice(inventoryDTO.getSalePrice())
                .build();
    }
}
