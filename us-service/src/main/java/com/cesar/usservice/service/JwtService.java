package com.cesar.usservice.service;

import com.cesar.usservice.dto.UserDTO;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.function.Function;

public interface JwtService {
    String getToken(UserDTO user);
    String getEmailFromToken(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    <T> T getClaim(String token, Function<Claims, T> claimsResolver);
}
