package com.cesar.usservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/clients")
public class ClientController {


    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
