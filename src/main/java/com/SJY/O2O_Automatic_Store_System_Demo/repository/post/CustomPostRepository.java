package com.SJY.O2O_Automatic_Store_System_Demo.repository.post;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostReadCondition;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {
    Page<PostSimpleDto> findAllByCondition(PostReadCondition cond);
}