package com.SJY.O2O_Automatic_Store_System_Demo.factory.entity;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Image;

public class ImageFactory {
    public static Image createImage() {
        return new Image("origin_filename.jpg");
    }

    public static Image createImageWithOriginName(String originName) {
        return new Image(originName);
    }
}