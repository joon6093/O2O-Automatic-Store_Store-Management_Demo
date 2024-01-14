package com.SJY.O2O_Automatic_Store_System_Demo.controller.message;

import com.SJY.O2O_Automatic_Store_System_Demo.TestInitDB;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.message.MessageCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignInResponse;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.message.MessageRepository;
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
import com.SJY.O2O_Automatic_Store_System_Demo.exception.*;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class MessageControllerIntegrationTest {
    @Autowired
    WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestInitDB initDB;
    @Autowired
    SignService signService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MessageRepository messageRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    Member admin, sender, receiver, member;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();
        admin = memberRepository.findByEmail(initDB.getAdminEmail()).orElseThrow(MemberNotFoundException::new);
        sender = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        receiver = memberRepository.findByEmail(initDB.getMember2Email()).orElseThrow(MemberNotFoundException::new);
        member = memberRepository.findByEmail(initDB.getMember3Email()).orElseThrow(MemberNotFoundException::new);
    }

    @Test
    void readAllBySenderTest() throws Exception{
        // given
        Integer size = 2;
        SignInResponse signInRes = signService.signIn(createSignInRequest(sender.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        get("/api/messages/sender")
                                .param("size", String.valueOf(size))
                                .header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.numberOfElements").value(2));
    }

    @Test
    void readAllBySenderUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Integer size = 2;

        // when, then
        mockMvc.perform(
                        get("/api/messages/sender")
                                .param("size", String.valueOf(size)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void readAllByReceiverTest() throws Exception {
        // given
        Integer size = 2;
        SignInResponse signInRes = signService.signIn(createSignInRequest(receiver.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        get("/api/messages/receiver")
                                .param("size", String.valueOf(size))
                                .header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.numberOfElements").value(2));
    }

    @Test
    void readAllByReceiverUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Integer size = 2;

        // when, then
        mockMvc.perform(
                        get("/api/messages/receiver")
                                .param("size", String.valueOf(size)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void readByResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse senderSignInRes = signService.signIn(createSignInRequest(sender.getEmail(), initDB.getPassword()));
        SignInResponse receiverSignInRes = signService.signIn(createSignInRequest(receiver.getEmail(), initDB.getPassword()));
        // when, then
        mockMvc.perform(
                        get("/api/messages/{id}", id)
                                .header("Authorization", senderSignInRes.getAccessToken()))
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/api/messages/{id}", id)
                                .header("Authorization", receiverSignInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void readByAdminTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse adminSignInRes = signService.signIn(createSignInRequest(admin.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        get("/api/messages/{id}", id)
                                .header("Authorization", adminSignInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void readUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        // when, then
        mockMvc.perform(
                        get("/api/messages/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void readAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse notResourceOwnerSignInRes = signService.signIn(createSignInRequest(member.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        get("/api/messages/{id}", id)
                                .header("Authorization", notResourceOwnerSignInRes.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void createTest() throws Exception {
        // given
        MessageCreateRequest req = createMessageCreateRequest("content", null, receiver.getId());
        SignInResponse signInRes = signService.signIn(createSignInRequest(sender.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        post("/api/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                                .header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isCreated());
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        // given
        MessageCreateRequest req = createMessageCreateRequest("content", null, receiver.getId());

        // when, then
        mockMvc.perform(
                        post("/api/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBySenderByResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse signInRes = signService.signIn(createSignInRequest(sender.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/sender/{id}", id)
                                .header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBySenderByAdminTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse adminSignInRes = signService.signIn(createSignInRequest(admin.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/sender/{id}", id)
                                .header("Authorization", adminSignInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBySenderUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(
                        delete("/api/messages/sender/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBySenderAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse notResourceOwnerSignInRes = signService.signIn(createSignInRequest(receiver.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/sender/{id}", id)
                                .header("Authorization", notResourceOwnerSignInRes.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteByReceiverByResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse signInRes = signService.signIn(createSignInRequest(receiver.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id)
                                .header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByReceiverByAdminTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse adminSignInRes = signService.signIn(createSignInRequest(admin.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id)
                                .header("Authorization", adminSignInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByReceiverUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteByReceiverAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse notResourceOwnerSignInRes = signService.signIn(createSignInRequest(sender.getEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id)
                                .header("Authorization", notResourceOwnerSignInRes.getAccessToken()))
                .andExpect(status().isForbidden());
    }
}