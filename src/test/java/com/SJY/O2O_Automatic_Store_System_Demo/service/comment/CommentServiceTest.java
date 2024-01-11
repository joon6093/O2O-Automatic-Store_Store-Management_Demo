package com.SJY.O2O_Automatic_Store_System_Demo.service.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentDto;
import com.SJY.O2O_Automatic_Store_System_Demo.event.comment.CommentCreatedEvent;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CommentNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.PostNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.comment.CommentRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.CommentCreateRequestFactory.createCommentCreateRequest;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.CommentCreateRequestFactory.createCommentCreateRequestWithParentId;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CommentFactory.createComment;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CommentFactory.createDeletedComment;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.MemberFactory.createMember;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    CommentService commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    ApplicationEventPublisher publisher;

    @Test
    void readAllTest() {
        // given
        given(commentRepository.findAllTopLevelCommentsByPostId(anyLong()))
                .willReturn(
                        List.of(createComment(null),
                                createComment(null)
                        )
                );

        // when
        List<CommentDto> result = commentService.readAll(createCommentReadCondition());

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void readAllDeletedCommentTest() {
        // given
        given(commentRepository.findAllTopLevelCommentsByPostId(anyLong()))
                .willReturn(
                        List.of(createDeletedComment(null),
                                createDeletedComment(null)
                        )
                );

        // when
        List<CommentDto> result = commentService.readAll(createCommentReadCondition());

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getContent()).isNull();
        assertThat(result.get(0).getMember()).isNull();
    }

    @Test
    void createTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(createPost()));

        // when
        commentService.create(createCommentCreateRequest());

        // then
        verify(commentRepository).save(any());
        verify(publisher).publishEvent(any(CommentCreatedEvent.class));

    }

    @Test
    void createExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> commentService.create(createCommentCreateRequest()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void createExceptionByPostNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> commentService.create(createCommentCreateRequest()))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void createExceptionByCommentNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(createPost()));
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> commentService.create(createCommentCreateRequestWithParentId(1L)))
                .isInstanceOf(CommentNotFoundException.class);
    }

}