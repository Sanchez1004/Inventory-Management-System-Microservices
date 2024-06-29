package com.cesar.usservice.service;

import com.cesar.usservice.dto.UserDTO;
import com.cesar.usservice.dto.UserMapper;
import com.cesar.usservice.model.UserEntity;
import com.cesar.usservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserServiceImplementation(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> findUsersByRole(String role) {
        List<UserEntity> userEntities = userRepository.findAllByRole(role);
        return userEntities.stream().map(userMapper::toUserDTO).toList();
    }
}
