package com.cesar.invservice.entity;

import com.cesar.invservice.utils.HandleCategory;
import com.cesar.invservice.utils.Item;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventory")
public class InventoryEntity {
    @MongoId
    private String id;
    private Item item;
    private double salePrice;
    private HandleCategory handleCategory;
    private int threshold;

    // More prices can be added if needed

    @PrePersist
    @PreUpdate
    public void toUpperCase() {
        if (item.getName() != null) {
            this.item.setName(item.getName().toUpperCase());
        }
    }
}
