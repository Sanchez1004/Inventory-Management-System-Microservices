package com.cesar.usservice.service;

import com.cesar.usservice.exception.UserException;
import com.cesar.usservice.dto.UserDTO;
import com.cesar.usservice.dto.UserMapper;
import com.cesar.usservice.model.UserEntity;
import com.cesar.usservice.repository.UserRepository;
import com.cesar.usservice.utils.UserField;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class UserServiceImplementation implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final Map<UserField, BiConsumer <UserEntity, UserDTO>> updateFieldMap = new EnumMap<>(UserField.class);

    public UserServiceImplementation(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;

        initializeUpdateFieldMap();
    }

    private void initializeUpdateFieldMap() {
        updateFieldMap.put(UserField.FIRST_NAME, (entity, request) -> {
            if (request.getFirstName() != null) {
                entity.setFirstName(request.getFirstName());
            }
        });
        updateFieldMap.put(UserField.LAST_NAME, (entity, request) -> {
            if (request.getLastName() != null) {
                entity.setLastName(request.getLastName());
            }
        });
        updateFieldMap.put(UserField.EMAIL, (entity, request) -> {
            if (request.getEmail() != null) {
                entity.setEmail(request.getEmail());
            }
        });
        updateFieldMap.put(UserField.ROLE, (entity, request) -> {
            if (request.getRole() != null) {
                entity.setRole(request.getRole());
            }
        });
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
    public UserEntity getUserByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        return userEntity.orElseThrow(() -> new UserException("No user found with email: " + email));
    }

    @Override
    public void save(UserDTO user) {
        if (user != null) {
            if (user.getEmail() != null && user.getPassword() != null) {
                if (!userExistsByEmail(user.getEmail())) {
                    userRepository.save(userMapper.toUserEntity(user));
                }
            }
        }
        throw new UserException("User cannot be null");
    }

    @Override
    public void delete(String email) {
        UserDTO user = findByEmail(email);
        userRepository.delete(userMapper.toUserEntity(user));
    }

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        if (userDTO == null) {
            throw new UserException("User request cannot be null");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority();

        UserEntity userEntity = getUserById(id);

        for (Map.Entry<UserField, BiConsumer<UserEntity, UserDTO>> entry : updateFieldMap.entrySet()) {
            if (entry.getKey() == UserField.ROLE && !userRole.equals("ROLE_ADMIN")) {
                continue;
            }
            entry.getValue().accept(userEntity, userDTO);
        }

        userRepository.save(userEntity);
        return userMapper.toUserDTO(userEntity);
    }

    private UserEntity getUserById(String id) {
        return userRepository.findUserEntityById(id);
    }

    private boolean userExistsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException("User already exists");
        }
        return true;
    }
}
