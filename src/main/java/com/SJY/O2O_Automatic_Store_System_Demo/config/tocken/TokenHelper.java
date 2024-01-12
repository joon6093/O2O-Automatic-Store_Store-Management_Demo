package com.SJY.O2O_Automatic_Store_System_Demo.config.tocken;

import com.SJY.O2O_Automatic_Store_System_Demo.handler.JwtHandler;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenHelper {
    private final JwtHandler jwtHandler;
    private final String key;
    private final long maxAgeSeconds;
    private static final String MEMBER_ID = "MEMBER_ID";
    private static final String ROLE_TYPES = "ROLE_TYPES";
    private static final String SEP = ",";

    public String createToken(PrivateClaims privateClaims) {
        return jwtHandler.createToken(
                key,
                Map.of(MEMBER_ID, privateClaims.getMemberId(), ROLE_TYPES, String.join(SEP, privateClaims.getRoleTypes())),
                maxAgeSeconds
        );
    }

    public Optional<PrivateClaims> parse(String token) {
        return jwtHandler.parse(key, token).map(claims -> convert(claims));
    }

    private PrivateClaims convert(Claims claims) {
        return new PrivateClaims(
                claims.get(MEMBER_ID, String.class),
                Arrays.asList(claims.get(ROLE_TYPES, String.class).split(SEP))
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims {
        private String memberId;
        private List<String> roleTypes;
    }
}