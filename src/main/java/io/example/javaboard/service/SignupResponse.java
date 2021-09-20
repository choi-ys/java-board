package io.example.javaboard.service;

import io.example.javaboard.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : choi-ys
 * @date : 2021/09/21 3:22 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupResponse {

    private Long id;
    private String email;
    private String name;
    private String nickname;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/21 3:28 오전
    // * --------------------------------------------------------------
    public SignupResponse(Long id, String email, String name, String nickname) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
    }

    // * --------------------------------------------------------------
    // * Header : 객체 값 매핑
    // * @author : choi-ys
    // * @date : 2021/09/21 3:28 오전
    // * --------------------------------------------------------------
    public static SignupResponse mapTo(Member member) {
        return new SignupResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getNickname()
        );
    }
}
