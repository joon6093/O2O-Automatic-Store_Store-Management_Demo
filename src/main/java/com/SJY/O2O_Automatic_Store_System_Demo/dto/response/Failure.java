package com.SJY.O2O_Automatic_Store_System_Demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Failure implements Result {
    private String msg;
}