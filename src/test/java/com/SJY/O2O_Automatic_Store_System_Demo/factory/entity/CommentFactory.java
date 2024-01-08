package com.SJY.O2O_Automatic_Store_System_Demo.factory.entity;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.comment.Comment;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.MemberFactory.createMember;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.PostFactory.createPost;

public class CommentFactory {

    public static Comment createComment(Comment parent) {
        Comment comment = new Comment("content", createMember(), createPost());
        if(parent != null){
            parent.addChildComment(comment);
        }
        return comment;
    }

    public static Comment createDeletedComment(Comment parent) {
        Comment comment = new Comment("content", createMember(), createPost());
        comment.delete();
        return comment;
    }

    public static Comment createComment(Member member, Post post, Comment parent) {
        Comment comment = new Comment("content", member, post);
        if(parent != null){
            parent.addChildComment(comment);
        }
        return comment;
    }
}