package com.cesar.usservice.client;

import com.cesar.usservice.security.AuthUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client interface for interacting with the authentication service.
 * This client communicates with the "auth-service" to perform various authentication-related operations
 * such as token validation, email extraction from a token, and retrieving user details by email.
 */
@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    /**
     * Validates a token using the authentication service.
     *
     * @param token the token to validate
     * @param apiKey the API key to authorize the request
     * @return a ResponseEntity containing a Boolean indicating whether the token is valid
     */
    @GetMapping("/api/auth/validate-token")
    ResponseEntity<Boolean> validateToken(@RequestParam("token") String token, @RequestParam("apiKey") String apiKey);

    /**
     * Extracts the email from a token using the authentication service.
     *
     * @param token the token from which to extract the email
     * @param apiKey the API key to authorize the request
     * @return a ResponseEntity containing the email as a String
     */
    @GetMapping("/api/auth/extract-email")
    ResponseEntity<String> getEmailFromToken(@RequestParam("token") String token, @RequestParam("apiKey") String apiKey);

    /**
     * Retrieves user details by email using the authentication service.
     *
     * @param email the email of the user to retrieve
     * @param apiKey the API key to authorize the request
     * @return a ResponseEntity containing an AuthUserDTO object with user details
     */
    @GetMapping("/api/auth/get-user-by-email")
    ResponseEntity<AuthUserDTO> getUserByEmail(@RequestParam String email, @RequestParam String apiKey);
}
