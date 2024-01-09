package com.SJY.O2O_Automatic_Store_System_Demo.dto.member;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String username;
    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static MemberDto toDto(Member member) {
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getUsername(),
                member.getNickname(),
                member.getCreatedAt()
        );
    }
}