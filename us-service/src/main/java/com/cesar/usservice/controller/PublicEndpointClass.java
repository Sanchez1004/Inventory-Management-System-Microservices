package com.cesar.usservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple REST controller that exposes public endpoints which do not require user roles or authentication.
 * This controller can be used to provide access to basic information or test endpoints that are publicly accessible.
 */
@RestController
@RequestMapping("/api/users/no-auth")
public class PublicEndpointClass {

    /**
     * A simple endpoint that returns a greeting.
     * This endpoint is publicly accessible without any role or authentication required.
     *
     * @return a greeting string "Hello"
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
