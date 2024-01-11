package com.SJY.O2O_Automatic_Store_System_Demo;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Role;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.RoleType;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.message.Message;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.RoleNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.message.MessageRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.role.RoleRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestInitDB {
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final MessageRepository messageRepository;

    @Getter
    private final String adminEmail = "admin@admin.com";
    @Getter
    private final String member1Email = "member1@member.com";
    @Getter
    private final String member2Email = "member2@member.com";
    @Getter
    private final String member3Email = "member3@member.com";
    @Getter
    private final String password = "123456a!";

    @Transactional
    public void initDB() {
        initRole();
        initTestAdmin();
        initTestMember();
        initCategory();
        initMessage();
    }

    private void initRole() {
        roleRepository.saveAll(
                List.of(RoleType.values()).stream().map(roleType -> new Role(roleType)).collect(Collectors.toList())
        );
    }

    private void initTestAdmin() {
        memberRepository.save(
                new Member(adminEmail, passwordEncoder.encode(password), "admin", "admin",
                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN).orElseThrow(RoleNotFoundException::new)))
        );
    }

    private void initTestMember() {
        memberRepository.saveAll(
                List.of(
                        new Member(member1Email, passwordEncoder.encode(password), "member1", "member1",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))),
                        new Member(member2Email, passwordEncoder.encode(password), "member2", "member2",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))),
                        new Member(member3Email, passwordEncoder.encode(password), "member3", "member3",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new)))
                        )

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

    private void initMessage() {
        Member sender = memberRepository.findByEmail(getMember1Email()).orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByEmail(getMember2Email()).orElseThrow(MemberNotFoundException::new);
        IntStream.range(0, 5).forEach(i -> messageRepository.save(new Message("content" + i, sender, receiver)));
    }
}