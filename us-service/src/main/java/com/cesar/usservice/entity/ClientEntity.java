package com.cesar.usservice.entity;

import com.cesar.usservice.utils.Address;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "clients")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    @MongoId
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
}
