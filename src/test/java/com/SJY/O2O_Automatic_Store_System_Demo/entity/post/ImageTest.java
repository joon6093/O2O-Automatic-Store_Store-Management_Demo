package com.SJY.O2O_Automatic_Store_System_Demo.entity.post;

import com.SJY.O2O_Automatic_Store_System_Demo.exception.UnsupportedImageFormatException;
import org.junit.jupiter.api.Test;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.ImageFactory.*;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.PostFactory.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
class ImageTest {

    @Test
    void createImageTest() {
        // given
        String validExtension = "JPEG";

        // when, then
        createImageWithOriginName("image." + validExtension);
    }

    @Test
    void createImageExceptionByUnsupportedFormatTest() {
        // given
        String invalidExtension = "invalid";

        // when, then
        assertThatThrownBy(() -> createImageWithOriginName("image." + invalidExtension))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void createImageExceptionByNoneExtensionTest() {
        // given
        String originName = "image";

        // when, then
        assertThatThrownBy(() -> createImageWithOriginName(originName))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void initPostTest() {
        // given
        Image image = createImage();

        // when
        Post post = createPost();
        image.setPost(post);

        // then
        assertThat(image.getPost()).isSameAs(post);
    }

    @Test
    void initPostNotChangedTest() {
        // given
        Image image = createImage();
        image.setPost(createPost());

        // when
        Post post = createPost();
        image.setPost(post);
        // then
        assertThat(image.getPost()).isNotSameAs(post);
    }

}
