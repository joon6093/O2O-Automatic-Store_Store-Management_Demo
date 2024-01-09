package com.SJY.O2O_Automatic_Store_System_Demo;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.comment.Comment;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Role;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.RoleType;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.RoleNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.comment.CommentRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.post.PostRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.role.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("local")
public class InitDB {
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database");
        initRole();
        initTestAdmin();
        initTestMember();
        initCategory();
        initPost();
        initComment();
    }

    private void initRole() {
        roleRepository.saveAll(
                Stream.of(RoleType.values()).map(Role::new).collect(Collectors.toList())
        );
    }
    private void initTestAdmin() {
        memberRepository.save(
                new Member("admin@admin.com", passwordEncoder.encode("123456a!"), "admin", "admin",
                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN).orElseThrow(RoleNotFoundException::new)))
        );
    }

    private void initTestMember() {
        memberRepository.saveAll(
                List.of(
                        new Member("member1@member.com", passwordEncoder.encode("123456a!"), "member1", "member1",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))),
                        new Member("member2@member.com", passwordEncoder.encode("123456a!"), "member2", "member2",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))))
        );
    }

    private void initCategory() {
        Category c1 = categoryRepository.save(new Category("category1"));
        Category c2 = categoryRepository.save(new Category("category2"));
        c1.addChildCategory(c2);
        Category c3 = categoryRepository.save(new Category("category3"));
        c1.addChildCategory(c3);
        Category c4 = categoryRepository.save(new Category("category4"));
        c2.addChildCategory(c4);
        Category c5 = categoryRepository.save(new Category("category5"));
        c2.addChildCategory(c5);
        Category c6 = categoryRepository.save(new Category("category6"));
        c4.addChildCategory(c6);
        Category c7 = categoryRepository.save(new Category("category7"));
        c3.addChildCategory(c7);
        Category c8 = categoryRepository.save(new Category("category8"));
    }

    private void initPost() {
        Member member = memberRepository.findAll().get(1);
        Category category = categoryRepository.findAll().get(0);
        IntStream.range(0, 100)
                .forEach(i -> postRepository.save(
                        new Post("title" + i, "content" + i, (long) i, member, category)
                ));
    }

    private void initComment() {
        Member member = memberRepository.findAll().get(0);
        Post post = postRepository.findAll().get(0);
        Comment c1 = commentRepository.save(new Comment("content", member, post));
        Comment c2 = commentRepository.save(new Comment("content", member, post));
        c1.addChildComment(c2);
        Comment c3 = commentRepository.save(new Comment("content", member, post));
        c1.addChildComment(c3);
        Comment c4 = commentRepository.save(new Comment("content", member, post));
        c2.addChildComment(c4);
        Comment c5 = commentRepository.save(new Comment("content", member, post));
        c2.addChildComment(c5);
        Comment c6 = commentRepository.save(new Comment("content", member, post));
        c4.addChildComment(c6);
        Comment c7 = commentRepository.save(new Comment("content", member, post));
        c3.addChildComment(c7);
        Comment c8 = commentRepository.save(new Comment("content", member, post));
    }
}