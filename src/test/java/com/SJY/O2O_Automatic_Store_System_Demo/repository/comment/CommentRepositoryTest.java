package com.SJY.O2O_Automatic_Store_System_Demo.repository.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.config.QuerydslConfig;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.comment.Comment;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CommentNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.post.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CategoryFactory.createCategory;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CommentFactory.createComment;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.MemberFactory.createMember;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@Import(QuerydslConfig.class)
class CommentRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired CommentRepository commentRepository;
    @PersistenceContext
    EntityManager em;

    Member member;
    Category category;
    Post post;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(createMember());
        category = categoryRepository.save(createCategory());
        post = postRepository.save(createPost(member, category));
    }

    @Test
    void createAndReadTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        Comment foundComment = commentRepository.findById(comment.getId()).orElseThrow(CommentNotFoundException::new);
        clear();

        // then
        assertThat(foundComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    void deleteTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        commentRepository.deleteById(comment.getId());
        clear();

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByMemberTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        Comment childComment = commentRepository.save(createComment(member, post, comment));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
        assertThat(commentRepository.findById(childComment.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByPostTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByParentTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        Comment child = commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        commentRepository.deleteById(parent.getId());
        clear();

        // then
        assertThat(commentRepository.findById(child.getId())).isEmpty();
    }

    @Test
    void getChildrenTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        commentRepository.save(createComment(member, post, parent));
        commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        Comment comment = commentRepository.findById(parent.getId()).orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(comment.getChildren().size()).isEqualTo(2);
    }

    @Test
    void findWithParentByIdTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        Comment child = commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        Comment comment = commentRepository.findWithParentById(child.getId()).orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(comment.getParent()).isNotNull();
    }

    @Test
    void deleteCommentTest() {
        // given

        // root 1
        // 1 -> 2(del)
        // 2(del) -> 3(del)
        // 2(del) -> 4
        // 3(del) -> 5
        Comment comment1 = commentRepository.save(createComment(member, post, null));
        Comment comment2 = commentRepository.save(createComment(member, post, comment1));
        Comment comment3 = commentRepository.save(createComment(member, post, comment2));
        Comment comment4 = commentRepository.save(createComment(member, post, comment2));
        Comment comment5 = commentRepository.save(createComment(member, post, comment3));
        comment2.delete();
        comment3.delete();
        clear();

        // when
        Comment comment = commentRepository.findWithParentById(comment5.getId()).orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment().ifPresentOrElse(c -> commentRepository.delete(c), () -> comment5.delete());
        clear();

        // then
        List<Comment> comments = commentRepository.findAll();
        List<Long> commentIds = comments.stream().map(c -> c.getId()).toList();
        assertThat(commentIds.size()).isEqualTo(3);
    }

    @Test
    void deleteCommentQueryLogTest() {
        // given

        // 1(del) -> 2(del) -> 3(del) -> 4(del) -> 5
        Comment comment1 = commentRepository.save(createComment(member, post, null));
        Comment comment2 = commentRepository.save(createComment(member, post, comment1));
        Comment comment3 = commentRepository.save(createComment(member, post, comment2));
        Comment comment4 = commentRepository.save(createComment(member, post, comment3));
        Comment comment5 = commentRepository.save(createComment(member, post, comment4));
        comment1.delete();
        comment2.delete();
        comment3.delete();
        comment4.delete();
        clear();

        // when
        Comment comment = commentRepository.findWithParentById(comment5.getId()).orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment().ifPresentOrElse(c -> commentRepository.delete(c), () -> comment5.delete());
        clear();

        // then
        List<Comment> comments = commentRepository.findAll();
        List<Long> commentIds = comments.stream().map(c -> c.getId()).toList();
        assertThat(commentIds.size()).isEqualTo(0);
    }

    @Test
    void findAllTopLevelCommentsByPostIdTest() {
        // given
        Comment topLevelComment1 = commentRepository.save(createComment(member, post, null));
        Comment topLevelComment2 = commentRepository.save(createComment(member, post, null));
        Comment childComment = commentRepository.save(createComment(member, post, topLevelComment1));

        clear();

        // when
        List<Comment> topLevelComments = commentRepository.findAllTopLevelCommentsByPostId(post.getId());

        // then
        assertThat(topLevelComments).hasSize(2);
        assertThat(topLevelComments).extracting("id").containsOnly(topLevelComment1.getId(), topLevelComment2.getId());
    }

    void clear() {
        em.flush();
        em.clear();
    }

}