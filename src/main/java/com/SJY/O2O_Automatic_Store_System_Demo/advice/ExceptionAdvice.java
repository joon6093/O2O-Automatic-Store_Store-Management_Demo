package com.SJY.O2O_Automatic_Store_System_Demo.advice;


import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
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

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response> bindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(-1003, e.getMessage()));
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

    @ExceptionHandler(AuthenticationEntryPointException.class)
    public ResponseEntity<Response> authenticationEntryPoint() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Response.failure(-1001, "인증되지 않은 사용자입니다."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> accessDeniedException() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Response.failure(-1002, "접근이 거부되었습니다."));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Response> missingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(-1009, e.getHeaderName() + " 요청 헤더가 누락되었습니다."));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Response> categoryNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failure(-1010,"카테고리를 찾을 수 없습니다."));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Response> postNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failure(-1012, "요청한 게시글을 찾을 수 없습니다."));
    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    public ResponseEntity<Response> unsupportedImageFormatException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failure(-1013, "지원하지 않는 포멧입니다."));
    }

    @ExceptionHandler(FileUploadFailureException.class)
    public ResponseEntity<Response> fileUploadFailureException(FileUploadFailureException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failure(-1014, "파일 업로드에 실패하였습니다."));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Response> commentNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failure(-1015, "요청한 댓글을 찾을 수 없습니다."));
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<Response> messageNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failure(-1016, "존재하지 않는 쪽지입니다."));
    }
}