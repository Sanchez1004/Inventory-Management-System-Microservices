package com.cesar.usservice.dto;

import com.cesar.usservice.utils.Address;

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
public class SupplierDTO {
    private String id;
    private String name;
    private String email;
    private Address address;
    private double minQuota;
    private double maxQuota;
}
