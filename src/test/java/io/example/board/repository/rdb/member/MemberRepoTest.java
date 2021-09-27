package io.example.board.repository.rdb.member;

import io.example.board.config.test.DataJpaTestConfig;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.member.MemberRole;
import io.example.board.utils.generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/21 12:36 오전
 */
@DataJpaTestConfig
@DisplayName("Repo:Member")
class MemberRepoTest {

    private final MemberRepo memberRepo;
    private final EntityManager entityManager;

    public MemberRepoTest(MemberRepo memberRepo, EntityManager entityManager) {
        this.memberRepo = memberRepo;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("회원 객체 저장")
    public void save() {
        // Given
        Member member = MemberGenerator.member();

        // When
        Member expected = memberRepo.save(member);

        // Then
        assertAll(
                () -> assertNotNull(member.getId(), "Persist 상태의 Entity Id 항목의 Auto Generated 여부 확인"),
                () -> assertEquals(expected.getEmail(), member.getEmail()),
                () -> assertEquals(expected.getPassword(), member.getPassword()),
                () -> assertEquals(expected.getName(), member.getName()),
                () -> assertEquals(expected.getNickname(), member.getNickname()),
                () -> assertEquals(expected.getRoles(), Set.of(MemberRole.MEMBER), "Entity 객체 생성 시, 'MEMBER' 권한 포함 여부 확인"),
                () -> assertFalse(expected.isCertify(), "Entity 객체 생성 시, boolean 항목의 기본값 false 적용 여부 확인"),
                () -> assertFalse(expected.isEnabled(), "Entity 객체 생성 시 , boolean 항목의 기본값 false 적용 여부 확인"),
                () -> assertNotNull(expected.getCreatedBy(), "Auditor를 통해 설정되는 생성주체 정보의 null 여부를 확인"),
                () -> assertNotNull(expected.getCreatedAt(), "Auditor를 통해 설정되는 생성일자 정보의 null 여부를 확인"),
                () -> assertNotNull(expected.getUpdatedBy(), "Auditor를 통해 설정되는 수정주체 정보의 null 여부를 확인"),
                () -> assertNotNull(expected.getUpdatedAt(), "Auditor를 통해 설정되는 수정일자 정보의 null 여부를 확인")
        );
    }

    @Test
    @DisplayName("회원 객체 조회:Id")
    public void findById() {
        // Given
        Member member = MemberGenerator.member();
        Member savedMember = memberRepo.save(member);
        entityManager.flush();
        entityManager.clear();

        // When
        Member expected = memberRepo.findById(savedMember.getId()).orElseThrow();

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), savedMember.getId()),
                () -> assertEquals(expected.getEmail(), savedMember.getEmail()),
                () -> assertEquals(expected.getPassword(), savedMember.getPassword()),
                () -> assertEquals(expected.getName(), savedMember.getName()),
                () -> assertEquals(expected.getNickname(), savedMember.getNickname()),
                () -> assertEquals(expected.getRoles(), savedMember.getRoles()),
                () -> assertEquals(expected.isCertify(), savedMember.isCertify()),
                () -> assertEquals(expected.isEnabled(), savedMember.isEnabled())
        );
    }

    @Test
    @DisplayName("회원 객체 삭제")
    public void delete() {
        // Given
        Member member = MemberGenerator.member();
        Member savedMember = memberRepo.save(member);
        entityManager.flush();
        entityManager.clear();

        // When
        memberRepo.delete(member);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThrows(
                Exception.class,
                () -> memberRepo.findById(savedMember.getId()).get(), "Remove 상태의 Entity 조회 시 NoSuchElementException 발생 여부 확인"
        );
    }

    @Test
    @DisplayName("회원 객체 조회:Email")
    public void findByEmail() {
        // Given
        Member member = MemberGenerator.member();
        Member savedMember = memberRepo.save(member);
        entityManager.flush();
        entityManager.clear();

        // When
        Member expected = memberRepo.findByEmail(savedMember.getEmail()).orElseThrow();

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), savedMember.getId()),
                () -> assertEquals(expected.getEmail(), savedMember.getEmail()),
                () -> assertEquals(expected.getPassword(), savedMember.getPassword()),
                () -> assertEquals(expected.getName(), savedMember.getName()),
                () -> assertEquals(expected.getNickname(), savedMember.getNickname()),
                () -> assertEquals(expected.getRoles(), savedMember.getRoles()),
                () -> assertEquals(expected.isCertify(), savedMember.isCertify()),
                () -> assertEquals(expected.isEnabled(), savedMember.isEnabled())
        );
    }
}