package com.SJY.O2O_Automatic_Store_System_Demo.repository.member;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}