package com.cesar.ordservice.dto;

import com.cesar.ordservice.utils.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private String id;
    private String firstName;
    private String lastName;
    private Address address;
    private String mail;
}
