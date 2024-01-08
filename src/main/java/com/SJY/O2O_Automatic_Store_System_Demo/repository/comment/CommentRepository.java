package com.SJY.O2O_Automatic_Store_System_Demo.repository.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c left join fetch c.parent where c.id = :id")
    Optional<Comment> findWithParentById(@Param("id")Long id);

    @Query("select c from Comment c where c.post.id = :postId and c.parent is null")
    List<Comment> findAllTopLevelCommentsByPostId(@Param("postId") Long postId);
}