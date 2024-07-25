package com.cesar.ordservice.config;

import com.cesar.ordservice.client.AuthServiceClient;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final AuthServiceClient authServiceClient;

    @Value("${ADMIN_PASSWORD}")
    String adminPassword;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = authServiceClient.getServiceToken("admin@mail.com", adminPassword).getBody();
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
