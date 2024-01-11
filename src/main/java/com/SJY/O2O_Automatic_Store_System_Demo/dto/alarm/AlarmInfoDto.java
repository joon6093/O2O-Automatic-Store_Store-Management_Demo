package com.SJY.O2O_Automatic_Store_System_Demo.dto.alarm;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmInfoDto {
    private MemberDto target;
    private String message;
}