package com.SJY.O2O_Automatic_Store_System_Demo.controller.sign;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignInRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignUpRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.service.sign.SignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @PostMapping("/api/sign-up")
    public ResponseEntity<Response> signUp(@Valid @RequestBody SignUpRequest req) {
        signService.signUp(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success());
    }

    @PostMapping("/api/sign-in")
    public ResponseEntity<Response> signIn(@Valid @RequestBody SignInRequest req) {
        return ResponseEntity.ok(Response.success(signService.signIn(req)));
    }
}