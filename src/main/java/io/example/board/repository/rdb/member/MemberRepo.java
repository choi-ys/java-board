package io.example.board.repository.rdb.member;

import io.example.board.domain.rdb.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : choi-ys
 * @date : 2021/09/21 12:35 오전
 */
public interface MemberRepo extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
