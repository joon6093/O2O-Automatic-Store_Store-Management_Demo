package com.SJY.O2O_Automatic_Store_System_Demo.advice;


import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> exception(Exception e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(getFailureResponse("exception.code", "exception.msg"));
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    public ResponseEntity<Response> authenticationEntryPoint() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(getFailureResponse("authenticationEntryPoint.code", "authenticationEntryPoint.msg"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> accessDeniedException() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(getFailureResponse("accessDeniedException.code", "accessDeniedException.msg"));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response> bindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getFailureResponse("bindException.code", "bindException.msg", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getFailureResponse("bindException.code", "bindException.msg", Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<Response> loginFailureException() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(getFailureResponse("loginFailureException.code", "loginFailureException.msg"));
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    public ResponseEntity<Response> memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(getFailureResponse("memberEmailAlreadyExistsException.code", "memberEmailAlreadyExistsException.msg"));
    }

    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    public ResponseEntity<Response> memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(getFailureResponse("memberNicknameAlreadyExistsException.code", "memberNicknameAlreadyExistsException.msg", e.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Response> memberNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getFailureResponse("memberNotFoundException.code", "memberNotFoundException.msg"));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Response> roleNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getFailureResponse("roleNotFoundException.code", "roleNotFoundException.msg"));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Response> missingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getFailureResponse("missingRequestHeaderException.code", "missingRequestHeaderException.msg", e.getHeaderName()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Response> categoryNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getFailureResponse("categoryNotFoundException.code", "categoryNotFoundException.msg"));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Response> postNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getFailureResponse("postNotFoundException.code", "postNotFoundException.msg"));
    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    public ResponseEntity<Response> unsupportedImageFormatException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getFailureResponse("unsupportedImageFormatException.code", "unsupportedImageFormatException.msg"));
    }

    @ExceptionHandler(FileUploadFailureException.class)
    public ResponseEntity<Response> fileUploadFailureException(FileUploadFailureException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getFailureResponse("fileUploadFailureException.code", "fileUploadFailureException.msg"));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Response> commentNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getFailureResponse("commentNotFoundException.code", "commentNotFoundException.msg"));
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<Response> messageNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getFailureResponse("messageNotFoundException.code", "messageNotFoundException.msg"));
    }

    @ExceptionHandler(RefreshTokenFailureException.class)
    public ResponseEntity<Response> refreshTokenFailureException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getFailureResponse("refreshTokenFailureException.code", "refreshTokenFailureException.msg"));
    }

    private Response getFailureResponse(String codeKey, String messageKey) {
        log.info("code = {}, msg = {}", getCode(codeKey), getMessage(messageKey, null));
        return Response.failure(getCode(codeKey), getMessage(messageKey, null));
    }

    private Response getFailureResponse(String codeKey, String messageKey, Object... args) {
        return Response.failure(getCode(codeKey), getMessage(messageKey, args));
    }

    private Integer getCode(String key) {
        return Integer.valueOf(messageSource.getMessage(key, null, null));
    }


    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}