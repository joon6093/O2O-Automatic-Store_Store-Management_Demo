package com.SJY.O2O_Automatic_Store_System_Demo.config.tocken;

import com.SJY.O2O_Automatic_Store_System_Demo.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TokenConfig {

    private final JwtHandler jwtHandler;

    @Bean
    @Qualifier("accessTokenHelper")
    public TokenHelper accessTokenHelper(
            @Value("${jwt.key.access}") String key,
            @Value("${jwt.max-age.access}") long maxAgeSeconds) {
        return new TokenHelper(jwtHandler, key, maxAgeSeconds);
    }

    @Bean
    @Qualifier("refreshTokenHelper")
    public TokenHelper refreshTokenHelper(
            @Value("${jwt.key.refresh}") String key,
            @Value("${jwt.max-age.refresh}") long maxAgeSeconds) {
        return new TokenHelper(jwtHandler, key, maxAgeSeconds);
    }
}