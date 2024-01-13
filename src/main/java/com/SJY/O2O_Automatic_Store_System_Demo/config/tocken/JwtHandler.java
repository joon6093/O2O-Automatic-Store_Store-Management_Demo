package com.SJY.O2O_Automatic_Store_System_Demo.config.tocken;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class JwtHandler {
    private String type = "Bearer ";

    public String createToken(String key, Map<String, Object> privateClaims, long maxAgeSeconds) {
        Date now = new Date();
        return type + Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + maxAgeSeconds * 1000L))
                .addClaims(privateClaims)
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();
    }

    public Optional<Claims> parse(String key, String token) {
        try {
            return Optional.of(Jwts.parser().setSigningKey(key.getBytes()).parseClaimsJws(unType(token)).getBody());
        } catch (ExpiredJwtException e) {  // ExpiredJwtException 정보만 클라이언트에게 전달. 나머지 예외는 보안상에 이유르 전달하지 않음.
            log.warn("Expired JWT token - {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token - {}", e.getMessage());
            return Optional.empty();
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token - {}", e.getMessage());
            return Optional.empty();
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature - {}", e.getMessage());
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            log.warn("Invalid JWT token - {}", e.getMessage());
            return Optional.empty();
        } catch (JwtException e) {
            log.warn("JWT error - {}", e.getMessage());
            return Optional.empty();
        }
    }

    private String unType(String token) {
        return token.substring(type.length());
    }
}