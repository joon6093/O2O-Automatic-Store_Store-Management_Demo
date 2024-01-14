package com.SJY.O2O_Automatic_Store_System_Demo.service.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentCreateResponse;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentDto;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentReadCondition;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.comment.Comment;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CommentNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.PostNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.comment.CommentRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;

    public List<CommentDto> readAll(CommentReadCondition cond) {
        List<Comment> topLevelComments = commentRepository.findAllTopLevelCommentsByPostId(cond.getPostId());
        return topLevelComments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDto convertToDto(Comment comment) {
        List<CommentDto> childDtos = comment.getChildren().isEmpty()
                ? new ArrayList<>()
                : comment.getChildren().stream().map(this::convertToDto).collect(Collectors.toList());

        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.isDeleted() ? null : comment.getContent())
                .member(comment.isDeleted() ? null : MemberDto.toDto(comment.getMember()))
                .createdAt(comment.getCreatedAt())
                .children(childDtos)
                .build();
    }

    @Transactional
    public CommentCreateResponse create(CommentCreateRequest req) {
        Member member = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(req.getPostId()).orElseThrow(PostNotFoundException::new);
        Comment comment = Comment.builder()
                            .content(req.getContent())
                            .member(member)
                            .post(post)
                            .build();
        if(req.getParentId() != null) {
            Comment parent = commentRepository.findById(req.getParentId()).orElseThrow(CommentNotFoundException::new);
            parent.addChildComment(comment);

        }
        commentRepository.save(comment);
        comment.publishCreatedEvent(publisher);
        return new CommentCreateResponse(comment.getId());
    }

    @PreAuthorize("@commentGuard.check(#id)")
    @Transactional
    public void delete(@Param("id")Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment().ifPresentOrElse(
                deletableComment -> {
                    Optional.ofNullable(deletableComment.getParent())
                            .ifPresent(parent -> parent.removeChildComment(deletableComment));
                    commentRepository.delete(deletableComment);
                },
                comment::delete
        );
    }
}