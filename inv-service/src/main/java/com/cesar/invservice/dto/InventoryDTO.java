package com.cesar.invservice.dto;

import com.cesar.invservice.utils.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
    @MongoId
    private String id;
    private Item item;
    private double salePrice;
}
