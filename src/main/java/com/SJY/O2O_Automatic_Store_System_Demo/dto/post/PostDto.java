package com.SJY.O2O_Automatic_Store_System_Demo.dto.post;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private Long price;
    private MemberDto member;
    private List<ImageDto> images;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    @Builder
    public PostDto(Long id, String title, String content, Long price, MemberDto member, List<ImageDto> images, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.member = member;
        this.images = images;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}