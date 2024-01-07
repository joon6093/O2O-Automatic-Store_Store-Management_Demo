package com.SJY.O2O_Automatic_Store_System_Demo.service.post;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostDto;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostUpdateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Image;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CategoryNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.PostNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.UnsupportedImageFormatException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.post.PostRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CategoryFactory.createCategory;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.ImageFactory.createImage;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.ImageFactory.createImageWithIdAndOriginName;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.MemberFactory.createMember;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.PostFactory.createPostWithImages;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    PostService postService;
    @Mock
    PostRepository postRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    FileService fileService;

    @Test
    void createTest() {
        // given
        PostCreateRequest req = createPostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));
        given(postRepository.save(any())).willReturn(createPostWithImages(
                IntStream.range(0, req.getImages().size()).mapToObj(i -> createImage()).collect(toList()))
        );

        // when
        postService.create(req);

        // then
        verify(postRepository).save(any());
        verify(fileService, times(req.getImages().size())).upload(any(), anyString());
    }

    @Test
    void createExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> postService.create(createPostCreateRequest())).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void createExceptionByCategoryNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> postService.create(createPostCreateRequest())).isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void createExceptionByUnsupportedImageFormatExceptionTest() {
        // given
        PostCreateRequest req = createPostCreateRequestWithImages(
                List.of(new MockMultipartFile("test", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes()))
        );
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));

        // when, then
        assertThatThrownBy(() -> postService.create(req)).isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void readTest() {
        // given
        Post post = createPostWithImages(List.of(createImage(), createImage()));
        given(postRepository.findByIdWithMemberAndImages(anyLong())).willReturn(Optional.of(post));

        // when
        PostDto postDto = postService.read(1L);

        // then
        assertThat(postDto.getTitle()).isEqualTo(post.getTitle());
        assertThat(postDto.getImages().size()).isEqualTo(post.getImages().size());
    }

    @Test
    void readExceptionByPostNotFoundTest() {
        // given
        given(postRepository.findByIdWithMemberAndImages(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> postService.read(1L)).isInstanceOf(PostNotFoundException.class);
    }

    // PostServiceTest.java
    @Test
    void deleteTest() {
        // given
        Post post = createPostWithImages(List.of(createImage(), createImage()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        postService.delete(1L);

        // then
        verify(fileService, times(2)).delete(anyString());
        verify(postRepository).delete(any());
    }

    @Test
    void deleteExceptionByNotFoundPostTest() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> postService.delete(1L)).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void updateTest() {
        // given
        Image a = createImageWithIdAndOriginName(1L, "a.png");
        Image b = createImageWithIdAndOriginName(2L, "b.png");
        Post post = createPostWithImages(List.of(a, b));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "c".getBytes());
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest("title", "content", 1000L, List.of(cFile), List.of(a.getId()));

        // when
        postService.update(1L, postUpdateRequest);

        // then
        List<Image> images = post.getImages();
        List<String> originNames = images.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(originNames.size()).isEqualTo(2);
        assertThat(originNames).contains(b.getOriginName(), cFile.getOriginalFilename());

        verify(fileService, times(1)).upload(any(), anyString());
        verify(fileService, times(1)).delete(anyString());
    }

    @Test
    void updateExceptionByPostNotFoundTest() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> postService.update(1L, createPostUpdateRequest("title", "content", 1234L, List.of(), List.of())))
                .isInstanceOf(PostNotFoundException.class);
    }
}