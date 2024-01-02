package com.SJY.O2O_Automatic_Store_System_Demo.factory.dto;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.RefreshTokenResponse;

public class RefreshTokenResponseFactory {
    public static RefreshTokenResponse createRefreshTokenResponse(String accessToken) {
        return new RefreshTokenResponse(accessToken);
    }
}