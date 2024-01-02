package com.SJY.O2O_Automatic_Store_System_Demo.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenResponse {
    private String accessToken;
}