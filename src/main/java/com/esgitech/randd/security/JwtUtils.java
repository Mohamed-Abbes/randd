package com.esgitech.randd.security;

import com.esgitech.randd.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtils {

    private static final long EXPIRATION_TIME_MILLIS = 1000L * 60 * 60 * 24 * 30 * 6;  //Expiration in 6 months
    private SecretKey key;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    private void init(){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(String email, Role role, Long id, String userName) {
        return Jwts.builder()
                .subject(email)
                .claim("userName",userName)
                .claim("role", role)
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token){
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }

}
