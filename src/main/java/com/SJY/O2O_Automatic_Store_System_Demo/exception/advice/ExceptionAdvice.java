package com.SJY.O2O_Automatic_Store_System_Demo.exception.advice;


import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.*;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.response.ResponseHandler;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

import static com.SJY.O2O_Automatic_Store_System_Demo.exception.type.ExceptionType.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdvice {

    private final ResponseHandler responseHandler;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> exception(Exception e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(EXCEPTION));
    }

    @ExceptionHandler(AccessDeniedException.class) // @PreAuthorize으로 부터 발생하는 오류
    public ResponseEntity<Response> accessDeniedException(AccessDeniedException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(responseHandler.getFailureResponse(ACCESS_DENIED_EXCEPTION));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response> bindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(BIND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(BIND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<Response> loginFailureException() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseHandler.getFailureResponse(LOGIN_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    public ResponseEntity<Response> memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseHandler.getFailureResponse(MEMBER_EMAIL_ALREADY_EXISTS_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    public ResponseEntity<Response> memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseHandler.getFailureResponse(MEMBER_NICKNAME_ALREADY_EXISTS_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Response> memberNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(MEMBER_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Response> roleNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(ROLE_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Response> missingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(MISSING_REQUEST_HEADER_EXCEPTION, e.getHeaderName()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Response> categoryNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(CATEGORY_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(ExpiredJwtException.class) // refreshTokenHelper.parse(rToken)으로 부터 발생하는 오류
    public ResponseEntity<Response> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseHandler.getFailureResponse(EXPIRED_JWT_EXCEPTION));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Response> postNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(POST_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    public ResponseEntity<Response> unsupportedImageFormatException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(UNSUPPORTED_IMAGE_FORMAT_EXCEPTION));
    }

    @ExceptionHandler(FileUploadFailureException.class)
    public ResponseEntity<Response> fileUploadFailureException(FileUploadFailureException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(FILE_UPLOAD_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(FileDeleteFailureException.class)
    public ResponseEntity<Response> fileDeleteFailureException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(FILE_DELETE_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Response> commentNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(COMMENT_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<Response> messageNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(MESSAGE_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(RefreshTokenFailureException.class)
    public ResponseEntity<Response> refreshTokenFailureException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(REFRESH_TOKEN_FAILURE_EXCEPTION));
    }
}