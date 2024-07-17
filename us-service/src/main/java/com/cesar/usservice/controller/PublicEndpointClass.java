package com.cesar.usservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/no-role")
public class PublicEndpointClass {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
