package com.SJY.O2O_Automatic_Store_System_Demo.controller.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.TestInitDB;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentDto;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignInResponse;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.comment.Comment;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.comment.CommentRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.post.PostRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.service.comment.CommentService;
import com.SJY.O2O_Automatic_Store_System_Demo.service.sign.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.CommentCreateRequestFactory.createCommentCreateRequest;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CommentFactory.createComment;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
class CommentControllerIntegrationTest {
    @Autowired
    WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestInitDB initDB;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    SignService signService;
    ObjectMapper objectMapper = new ObjectMapper();

    Member member1, member2, admin;
    Category category;
    Post post;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();

        member1 = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        member2 = memberRepository.findByEmail(initDB.getMember2Email()).orElseThrow(MemberNotFoundException::new);
        admin = memberRepository.findByEmail(initDB.getAdminEmail()).orElseThrow(MemberNotFoundException::new);
        category = categoryRepository.findAll().get(0);
        post = postRepository.save(createPost(member1, category));
    }

    @Test
    void readAllTest() throws Exception {
        // given, when, then
        mockMvc.perform(
                        get("/api/comments").param("postId", String.valueOf(1)))
                .andExpect(status().isOk());
    }

    @Test
    void createTest() throws Exception {
        // given
        CommentCreateRequest req = createCommentCreateRequest("content", post.getId(), null, null);
        SignInResponse signInRes = signService.signIn(createSignInRequest(initDB.getMember1Email(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        post("/api/comments")
                                .header("Authorization", signInRes.getAccessToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        List<CommentDto> result = commentService.readAll(createCommentReadCondition(post.getId()));
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        // given
        CommentCreateRequest req = createCommentCreateRequest("content", post.getId(), member1.getId(), null);

        // when, then
        mockMvc.perform(
                        post("/api/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void deleteByResourceOwnerTest() throws Exception {
        // given
        Comment comment = commentRepository.save(createComment(member1, post, null));
        SignInResponse signInRes = signService.signIn(createSignInRequest(member1.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/comments/{id}", comment.getId())
                                .header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteByAdminTest() throws Exception {
        // given
        Comment comment = commentRepository.save(createComment(member1, post, null));
        SignInResponse adminSignInRes = signService.signIn(createSignInRequest(admin.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/comments/{id}", comment.getId())
                                .header("Authorization", adminSignInRes.getAccessToken()))
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Comment comment = commentRepository.save(createComment(member1, post, null));

        // when, then
        mockMvc.perform(delete("/api/comments/{id}", comment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Comment comment = commentRepository.save(createComment(member1, post, null));
        SignInResponse notOwnerSignInRes = signService.signIn(createSignInRequest(member2.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/comments/{id}", comment.getId())
                                .header("Authorization", notOwnerSignInRes.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }
}