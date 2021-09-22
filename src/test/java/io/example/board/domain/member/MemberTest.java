package io.example.board.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/21 12:00 오전
 */
@DisplayName("Entity:Member")
class MemberTest {

    @Test
    @DisplayName("회원 객체 생성")
    public void member() {
        // Given
        String email = "project.log.062@gmail.com";
        String password = "password";
        String name = "choi-ys";
        String nickname = "whypie";

        // When
        Member member = new Member(email, password, name, nickname);

        // Then
        assertAll(
                () -> assertEquals(member.getEmail(), email),
                () -> assertEquals(member.getPassword(), password),
                () -> assertEquals(member.getName(), name),
                () -> assertEquals(member.getNickname(), nickname),
                () -> assertEquals(member.getRoles(), Set.of(MemberRole.MEMBER), "Entity 객체 생성 시, 'MEMBER' 권한 포함 여부 확인"),
                () -> assertNull(member.getId(), "Entity 객체 생성 시, Id 항목의 Null 여부 확인"),
                () -> assertFalse(member.isCertify(), "Entity 객체 생성 시, boolean 항목의 기본값 false 적용 여부 확인"),
                () -> assertFalse(member.isEnabled(), "Entity 객체 생성 시 , boolean 항목의 기본값 false 적용 여부 확인")
        );
    }
}