package com.SJY.O2O_Automatic_Store_System_Demo.dto.member;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {
    private Long id;
    private String email;
    private String username;
    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Builder
    public MemberDto(Long id, String email, String username, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public static MemberDto empty() {
        return MemberDto.builder()
                .id(null)
                .email("")
                .username("")
                .nickname("")
                .createdAt(null)
                .build();
    }
}