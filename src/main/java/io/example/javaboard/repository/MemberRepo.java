package io.example.javaboard.repository;

import io.example.javaboard.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : choi-ys
 * @date : 2021/09/21 12:35 오전
 */
public interface MemberRepo extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
}
