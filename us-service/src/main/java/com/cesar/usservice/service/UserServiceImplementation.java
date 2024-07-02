package com.cesar.usservice.service;

import com.cesar.usservice.exception.UserException;
import com.cesar.usservice.dto.UserDTO;
import com.cesar.usservice.dto.UserMapper;
import com.cesar.usservice.model.UserEntity;
import com.cesar.usservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public UserDTO findByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        Optional<UserDTO> userDTO = userEntity.map(userMapper::toUserDTO);
        return userDTO.orElseThrow(() -> new UserException("No user found with email: " + email));
    }

    @Override
    public void save(UserDTO user) {
        if (user == null) {
            throw new UserException("User cannot be null");
        }

        userRepository.save(userMapper.toUserEntity(user));
    }
}
