package com.cesar.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth2")
public class TestController {

    @GetMapping("/hello")
    String hello() {
        return "hello";
    }
}
