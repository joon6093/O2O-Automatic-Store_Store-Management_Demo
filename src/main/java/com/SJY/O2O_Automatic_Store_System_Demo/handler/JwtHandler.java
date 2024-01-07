package com.SJY.O2O_Automatic_Store_System_Demo.handler;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
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
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token - {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token - {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token - {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature - {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("Invalid JWT token - {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("JWT error - {}", e.getMessage());
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