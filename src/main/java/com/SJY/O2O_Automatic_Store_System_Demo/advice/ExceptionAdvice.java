package com.SJY.O2O_Automatic_Store_System_Demo.advice;


import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> exception(Exception e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.failure(-1000, "오류가 발생하였습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(-1003, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<Response> loginFailureException() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Response.failure(-1004, "로그인에 실패하였습니다."));
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    public ResponseEntity<Response> memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.failure(-1005, e.getMessage() + "은 중복된 이메일 입니다."));
    }

    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    public ResponseEntity<Response> memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.failure(-1006, e.getMessage() + "은 중복된 닉네임 입니다."));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Response> memberNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failure(-1007, "요청한 회원을 찾을 수 없습니다."));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Response> roleNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failure(-1008, "요청한 권한 등급을 찾을 수 없습니다."));
    }
}