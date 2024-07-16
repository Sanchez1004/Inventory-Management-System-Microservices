package com.cesar.usservice.entity;

import com.cesar.usservice.dto.OrderDetails;
import com.cesar.usservice.utils.Address;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "clients")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    @MongoId
    private String id; // We are going to add a custom id that's going to be the NIT or CC
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
    private List<OrderDetails> pendingOrders;
}
