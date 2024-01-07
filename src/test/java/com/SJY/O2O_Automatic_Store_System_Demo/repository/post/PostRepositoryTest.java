package com.SJY.O2O_Automatic_Store_System_Demo.repository.post;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostUpdateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Image;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.PostNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CategoryFactory.createCategory;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.ImageFactory.createImage;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.ImageFactory.createImageWithOriginName;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.MemberFactory.createMember;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.PostFactory.createPost;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.PostFactory.createPostWithImages;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageRepository imageRepository;
    @PersistenceContext
    EntityManager em;

    Member member;
    Category category;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(createMember());
        category = categoryRepository.save(createCategory());
    }

    @Test
    void createAndReadTest() {
        // given
        Post post = postRepository.save(createPost(member, category));
        clear();

        // when
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);

        // then
        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void deleteTest() {
        // given
        Post post = postRepository.save(createPost(member, category));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then
        assertThatThrownBy(() -> postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void createCascadeImageTest() {
        // given
        Post post = postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);

        // then
        List<Image> images = foundPost.getImages();
        assertThat(images.size()).isEqualTo(2);
    }

    @Test
    void deleteCascadeImageTest() {
        // given
        Post post = postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then
        List<Image> images = imageRepository.findAll();
        assertThat(images.size()).isZero();
    }

    @Test
    void deleteCascadeByMemberTest() {
        // given
        postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        List<Post> result = postRepository.findAll();
        assertThat(result.size()).isZero();
    }

    @Test
    void deleteCascadeByCategoryTest() {
        // given
        postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        categoryRepository.deleteById(category.getId());
        clear();

        // then
        List<Post> result = postRepository.findAll();
        assertThat(result.size()).isZero();
    }

    @Test
    void findByIdWithMemberAndImagesTest() {
        // given
        Post post = postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        Post foundPost = postRepository.findByIdWithMemberAndImages(post.getId()).orElseThrow(PostNotFoundException::new);
        clear();

        // then
        Member foundMember = foundPost.getMember();
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
        List<Image> foundImage = foundPost.getImages();
        assertThat(foundImage.size()).isEqualTo(2);
    }

    @Test
    void updateTest() {
        // given
        Image a = createImageWithOriginName("a.jpg");
        Image b = createImageWithOriginName("b.png");
        Post post = postRepository.save(createPostWithImages(member, category, List.of(a, b)));
        clear();

        // when
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "cFile".getBytes());
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest("update title", "update content", 1234L, List.of(cFile), List.of(a.getId()));
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
        foundPost.update(postUpdateRequest);
        clear();

        // then
        Post result = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
        assertThat(result.getTitle()).isEqualTo(postUpdateRequest.getTitle());
        assertThat(result.getContent()).isEqualTo(postUpdateRequest.getContent());
        assertThat(result.getPrice()).isEqualTo(postUpdateRequest.getPrice());
        List<Image> images = result.getImages();
        List<String> originNames = images.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(images.size()).isEqualTo(2);
        assertThat(originNames).contains(b.getOriginName(), cFile.getOriginalFilename());
        List<Image> resultImages = imageRepository.findAll();
        assertThat(resultImages.size()).isEqualTo(2);
    }

    void clear() {
        em.flush();
        em.clear();
    }
}