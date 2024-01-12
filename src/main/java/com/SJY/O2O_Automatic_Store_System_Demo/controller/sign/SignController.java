package com.SJY.O2O_Automatic_Store_System_Demo.controller.sign;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignInRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignUpRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.service.sign.SignService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @Operation(summary = "회원가입", description = "회원가입을 한다.")
    @PostMapping("/api/sign-up")
    public ResponseEntity<Response> signUp(@Valid @RequestBody SignUpRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(signService.signUp(req)));
    }

    @Operation(summary = "로그인", description = "로그인을 한다.")
    @PostMapping("/api/sign-in")
    public ResponseEntity<Response> signIn(@Valid @RequestBody SignInRequest req) {
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(signService.signIn(req)));
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급 받는다.")
    @PostMapping("/api/refresh-token")
    public ResponseEntity<Response> refreshToken(@RequestHeader(value = "Authorization") String refreshToken) {
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(signService.refreshToken(refreshToken)));
    }
}