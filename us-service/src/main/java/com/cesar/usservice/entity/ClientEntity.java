package com.cesar.usservice.entity;

import com.cesar.usservice.utils.OrderDetails;
import com.cesar.usservice.utils.Address;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "clients")
public class ClientEntity {
    @Id
    private String id; // We are going to add a custom id that's going to be the NIT or CC
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
    private List<OrderDetails> pendingOrders;
}
