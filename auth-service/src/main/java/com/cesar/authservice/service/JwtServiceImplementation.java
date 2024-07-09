package com.cesar.authservice.service;

import com.cesar.authservice.entity.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.builder;

@Service
public class JwtServiceImplementation implements JwtService {

    @Value("${secret.key}")
    private String secretKey;

    private static final long TOKEN_VALIDITY = Duration.ofDays(1).toMillis();

    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String getEmailFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    @Override
    public String getRoleFromToken(String token) {
        return getAllClaims(token).get("ROLE", String.class);
    }

    private Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    @Override
    public String getToken(AuthUser authUser) {
        return getToken(new HashMap<>(), authUser);
    }

    private String getToken(Map<String, Object> extraClaims, AuthUser authUser) {
        return builder()
                .claims(extraClaims)
                .id(authUser.getId())
                .claim("ROLE", authUser.getRole())
                .subject(authUser.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

