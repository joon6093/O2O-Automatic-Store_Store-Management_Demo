package com.SJY.O2O_Automatic_Store_System_Demo.dto.post;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Image;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Schema(description = "게시글 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @Schema(description = "게시글 제목", example = "my title", required = true)
    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String title;

    @Schema(description = "게시글 본문", example = "my content", required = true)
    @NotBlank(message = "게시글 본문을 입력해주세요.")
    private String content;

    @Schema(description = "가격", example = "50000", required = true)
    @NotNull(message = "가격을 입력해주세요.")
    @PositiveOrZero(message = "0원 이상을 입력해주세요")
    private Long price;

    @Schema(hidden = true)
    private Long memberId;

    @Schema(description = "카테고리 아이디", example = "3", required = true)
    @NotNull(message = "카테고리 아이디를 입력해주세요.")
    @PositiveOrZero(message = "올바른 카테고리 아이디를 입력해주세요.")
    private Long categoryId;

    @Schema(description = "이미지", type = "array", format = "binary")
    private List<MultipartFile> images = new ArrayList<>();
    public static Post toEntity(PostCreateRequest req, MemberRepository memberRepository, CategoryRepository categoryRepository) {
        Post post = new Post(
                req.title,
                req.content,
                req.price,
                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new),
                categoryRepository.findById(req.getCategoryId()).orElseThrow(CategoryNotFoundException::new)
        );
        post.addImages(req.images.stream().map(i -> new Image(i.getOriginalFilename())).collect(toList()));
        return post;
    }
}