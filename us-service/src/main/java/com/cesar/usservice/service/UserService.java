package com.cesar.usservice.service;

import com.cesar.usservice.dto.UserDTO;
import com.cesar.usservice.model.UserEntity;

import java.util.List;

public interface UserService {
    List<UserDTO> findUsersByRole(String role);

    UserDTO findByEmail(String email);

    void save(UserDTO user);
    void delete(String email);

    UserDTO updateUser(String id, UserDTO userDTO);

    UserEntity getUserByEmail(String email);
}
