package com.SJY.O2O_Automatic_Store_System_Demo.repository.post;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    @Query("select p from Post p join fetch p.member left join fetch p.images where p.id = :id")
    Optional<Post> findByIdWithMemberAndImages(@Param("id") Long id);
}
