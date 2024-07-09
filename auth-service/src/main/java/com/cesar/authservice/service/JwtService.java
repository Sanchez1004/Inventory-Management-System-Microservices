package com.cesar.authservice.service;

import com.cesar.authservice.entity.AuthUser;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.function.Function;

public interface JwtService {
    String getToken(AuthUser authUser);
    String getEmailFromToken(String token);
    String getRoleFromToken(String token);
    Boolean isTokenValid(String token, UserDetails userDetails);
    <T> T getClaim(String token, Function<Claims, T> claimsResolver);
}
