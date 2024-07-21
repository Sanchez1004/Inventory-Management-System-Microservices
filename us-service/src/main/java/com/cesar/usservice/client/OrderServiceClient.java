package com.cesar.usservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ord-service")
public interface OrderServiceClient {
}
