package com.SJY.O2O_Automatic_Store_System_Demo.factory.dto;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignInRequest;

public class SignInRequestFactory {
    public static SignInRequest createSignInRequest() {
        return new SignInRequest("email@email.com", "123456a!");
    }

    public static SignInRequest createSignInRequest(String email, String password) {
        return new SignInRequest(email, password);
    }

    public static SignInRequest createSignInRequestWithEmail(String email) {
        return new SignInRequest(email, "123456a!");
    }

    public static SignInRequest createSignInRequestWithPassword(String password) {
        return new SignInRequest("email@email.com", password);
    }
}