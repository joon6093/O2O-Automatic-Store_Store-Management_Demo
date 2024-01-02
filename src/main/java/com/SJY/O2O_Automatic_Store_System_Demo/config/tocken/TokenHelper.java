package com.SJY.O2O_Automatic_Store_System_Demo.config.tocken;

import com.SJY.O2O_Automatic_Store_System_Demo.handler.JwtHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenHelper {
    private final JwtHandler jwtHandler;
    private final String key;
    private final long maxAgeSeconds;

    public String createToken(String subject) {
        return jwtHandler.createToken(key, subject, maxAgeSeconds);
    }

    public boolean validate(String token) {
        return jwtHandler.validate(key, token);
    }

    public String extractSubject(String token) {
        return jwtHandler.extractSubject(key, token);
    }
}