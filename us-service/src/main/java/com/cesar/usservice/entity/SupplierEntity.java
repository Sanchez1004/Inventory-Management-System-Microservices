package com.cesar.usservice.entity;

import com.cesar.usservice.utils.Address;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "suppliers")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierEntity {
    @MongoId
    private String id; // We are going to add a custom id that's going to be the NIT or CC
    private String name;
    private String email;
    private Address address;
    private double minQuota;
    private double maxQuota;
}
