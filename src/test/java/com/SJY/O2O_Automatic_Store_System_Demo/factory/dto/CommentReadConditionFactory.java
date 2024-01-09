package com.SJY.O2O_Automatic_Store_System_Demo.factory.dto;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentReadCondition;

public class CommentReadConditionFactory {
    public static CommentReadCondition createCommentReadCondition() {
        return new CommentReadCondition(1L);
    }

    public static CommentReadCondition createCommentReadCondition(Long postId) {
        return new CommentReadCondition(postId);
    }
}