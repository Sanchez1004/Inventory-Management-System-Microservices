package com.cesar.usservice.dto;

import com.cesar.usservice.model.UserEntity;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Builder
@Component
public class UserMapper {
    public UserEntity toUserEntity(UserDTO userDTO) {
        return UserEntity
                .builder()
                .lastName(userDTO.getLastName())
                .firstName(userDTO.getFirstName())
                .role(userDTO.getRole())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
    }

    public UserDTO toUserDTO(UserEntity userEntity) {
        return UserDTO
                .builder()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .build();
    }
}
