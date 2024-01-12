package com.SJY.O2O_Automatic_Store_System_Demo.dto.post;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Image;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageDto {
    private Long id;
    private String originName;
    private String uniqueName;

    @Builder
    public ImageDto(Long id, String originName, String uniqueName) {
        this.id = id;
        this.originName = originName;
        this.uniqueName = uniqueName;
    }

    public static ImageDto toDto(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .originName(image.getOriginName())
                .uniqueName(image.getUniqueName())
                .build();
    }
}