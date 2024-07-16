package com.cesar.usservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
}
