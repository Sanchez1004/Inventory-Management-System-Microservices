package com.cesar.ordservice.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
