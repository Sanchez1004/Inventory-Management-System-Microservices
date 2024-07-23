package com.cesar.usservice.dto;

import com.cesar.usservice.utils.Address;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
    private List<OrderDetailsDTO> pendingOrders;
}
