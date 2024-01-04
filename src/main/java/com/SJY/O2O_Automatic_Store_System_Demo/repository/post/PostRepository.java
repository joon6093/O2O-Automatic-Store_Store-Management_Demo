package com.SJY.O2O_Automatic_Store_System_Demo.repository.post;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}