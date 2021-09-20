package io.example.javaboard.domain.dto.request;

import io.example.javaboard.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : choi-ys
 * @date : 2021/09/21 3:21 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/21 3:34 오전
    // * --------------------------------------------------------------
    public SignupRequest(String email, String password, String name, String nickname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
    }

    // * --------------------------------------------------------------
    // * Header : 객체 값 매핑
    // * @author : choi-ys
    // * @date : 2021/09/21 3:31 오전
    // * --------------------------------------------------------------
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return new Member(
                email,
                passwordEncoder.encode(password),
                name,
                nickname
        );
    }
}
