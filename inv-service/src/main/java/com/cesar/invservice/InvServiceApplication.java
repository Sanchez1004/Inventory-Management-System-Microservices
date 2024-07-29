package com.cesar.invservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
public class InvServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvServiceApplication.class, args);
    }
}
