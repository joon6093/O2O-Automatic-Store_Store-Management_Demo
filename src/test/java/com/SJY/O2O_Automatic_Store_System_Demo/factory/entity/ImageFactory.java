package com.SJY.O2O_Automatic_Store_System_Demo.factory.entity;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Image;
import org.springframework.test.util.ReflectionTestUtils;

public class ImageFactory {
    public static Image createImage() {
        return new Image("origin_filename.jpg");
    }

    public static Image createImageWithOriginName(String originName) {
        return new Image(originName);
    }

    public static Image createImageWithIdAndOriginName(Long id, String originName) {
        Image image = new Image(originName);
        ReflectionTestUtils.setField(image, "id", id);
        return image;
    }
}