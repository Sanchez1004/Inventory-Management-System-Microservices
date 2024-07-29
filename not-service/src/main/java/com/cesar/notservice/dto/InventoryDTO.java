package com.cesar.notservice.dto;

import com.cesar.notservice.utils.HandleCategory;
import com.cesar.notservice.utils.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
    private String id;
    private Item item;
    private double salePrice;
    private HandleCategory handleCategory;
}
