package com.cesar.invservice.dto;

import com.cesar.invservice.utils.HandleCategory;
import com.cesar.invservice.utils.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private int threshold;
}
