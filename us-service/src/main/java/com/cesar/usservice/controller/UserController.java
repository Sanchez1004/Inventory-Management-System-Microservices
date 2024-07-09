package com.cesar.usservice.controller;

import com.cesar.usservice.exception.UserException;
import com.cesar.usservice.dto.UserDTO;
import com.cesar.usservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDTO>> findAllUsersByRole(@RequestParam String role) {
        try {
            List<UserDTO> listOfUsers = userService.findUsersByRole(role);
            return ResponseEntity.ok(listOfUsers);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/find-by-email")
    public ResponseEntity<UserDTO> findByEmail(@RequestParam String email) {
        UserDTO userFound = userService.findByEmail(email);
        return ResponseEntity.ok(userFound);
    }

    @PutMapping("/save")
    public ResponseEntity<Void> saveUser(@RequestBody UserDTO userDTO) {
        try {
            userService.save(userDTO);
            return ResponseEntity.ok().build();
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
