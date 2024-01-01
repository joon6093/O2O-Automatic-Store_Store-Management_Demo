package com.SJY.O2O_Automatic_Store_System_Demo.handler;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHandler {

    private final String type = "Bearer ";

    public String createToken(String encodedKey, String subject, long maxAgeSeconds) {
        Date now = new Date();
        return type + Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + maxAgeSeconds * 1000L))
                .signWith(SignatureAlgorithm.HS256, encodedKey)
                .compact();
    }

    public String extractSubject(String encodedKey, String token) {
        return parse(encodedKey, token).getBody().getSubject();
    }

    public boolean validate(String encodedKey, String token) {
        try {
            parse(encodedKey, token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Jws<Claims> parse(String encodedKey, String token) {
        return Jwts.parser()
                .setSigningKey(encodedKey)
                .parseClaimsJws(untyped(token));
    }

    private String untyped(String token) {
        return token.substring(type.length());
    }

}