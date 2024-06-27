package com.cesar.usservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsServiceApplication.class, args);
    }

}
