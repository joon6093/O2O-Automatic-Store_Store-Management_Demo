package com.SJY.O2O_Automatic_Store_System_Demo.entity.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.common.EntityDate;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import com.SJY.O2O_Automatic_Store_System_Demo.event.comment.CommentCreatedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Comment extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private Set<Comment> children = new HashSet<>();

    @Builder
    public Comment(String content, Member member, Post post) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.deleted = false;
    }

    private void setParent(Comment parent) {
        this.parent = parent;
    }

    public void addChildComment(Comment child) {
        getChildren().add(child);
        child.setParent(this);
    }

    public void removeChildComment(Comment child) {
        getChildren().remove(child);
        child.setParent(null);
    }

    public Optional<Comment> findDeletableComment() {
        return hasChildren() ? Optional.empty() : Optional.of(findDeletableCommentByParent());
    }

    public void delete() {
        this.deleted = true;
    }

    private Comment findDeletableCommentByParent() {
        if (isDeletedParent()) {
            Comment deletableParent = getParent().findDeletableCommentByParent();
            if(getParent().getChildren().size() == 1) return deletableParent;
        }
        return this;
    }

    private boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    private boolean isDeletedParent() {
        return getParent() != null && getParent().isDeleted();
    }

    public void publishCreatedEvent(ApplicationEventPublisher publisher) {
        publisher.publishEvent(
                new CommentCreatedEvent(
                        MemberDto.toDto(getMember()),
                        MemberDto.toDto(getPost().getMember()),
                        Optional.ofNullable(getParent()).map(p -> p.getMember()).map(m -> MemberDto.toDto(m)).orElseGet(() -> MemberDto.empty()),
                        getContent()
                )
        );
    }
}