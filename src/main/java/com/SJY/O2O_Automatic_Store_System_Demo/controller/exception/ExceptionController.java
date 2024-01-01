package com.SJY.O2O_Automatic_Store_System_Demo.controller.exception;

import com.SJY.O2O_Automatic_Store_System_Demo.exception.AccessDeniedException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.AuthenticationEntryPointException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {
    @GetMapping("/exception/entry-point")
    public void entryPoint() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/exception/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException();
    }
}