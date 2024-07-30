package com.cesar.invservice.config;

import com.cesar.invservice.client.AuthServiceClient;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final AuthServiceClient authServiceClient;

    @Value("${spring.security.admin.password}")
    String adminPassword;

    @Value("${spring.security.admin.mail}")
    String adminMail;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = authServiceClient.getServiceToken(adminMail, adminPassword).getBody();
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
