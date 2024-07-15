package com.cesar.usservice.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String streetName;
    private String houseNumber;
    private String city;
    private String zipCode;
    private String neighborhood;
    private String state;
}
