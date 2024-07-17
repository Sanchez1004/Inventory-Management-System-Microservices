package com.cesar.usservice.controller;

import com.cesar.usservice.dto.ClientDTO;
import com.cesar.usservice.exception.ClientException;
import com.cesar.usservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/users/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/get-client")
    ResponseEntity<ClientDTO> getClientById(@RequestParam String id) {
        try {
            return
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
