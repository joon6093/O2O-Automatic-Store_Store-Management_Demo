package com.SJY.O2O_Automatic_Store_System_Demo.service.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentDto;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentReadCondition;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.comment.Comment;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CommentNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.PostNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.comment.CommentRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public List<CommentDto> readAll(CommentReadCondition cond) {
        return CommentDto.toDtoList(
                commentRepository.findAllTopLevelCommentsByPostId(cond.getPostId())
        );
    }

    @Transactional
    public void create(CommentCreateRequest req) {
        Comment comment = new Comment(
                req.getContent(),
                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new),
                postRepository.findById(req.getPostId()).orElseThrow(PostNotFoundException::new)
        );
        if(req.getParentId() != null) {
            Comment parent = commentRepository.findById(req.getParentId()).orElseThrow(CommentNotFoundException::new);
            parent.addChildComment(comment);

        }
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(Long id) {
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