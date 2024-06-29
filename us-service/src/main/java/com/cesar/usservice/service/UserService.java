package com.cesar.usservice.service;

import com.cesar.usservice.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findUsersByRole(String role);
}
