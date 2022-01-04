package io.example.board.domain.dto.request;

import io.example.board.domain.rdb.member.Member;
import io.example.board.validation.EmailUnique;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author : choi-ys
 * @date : 2021/09/21 3:21 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupRequest {
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "이메일 형식에 맞게 입력하세요.")
    @EmailUnique
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내로 입력하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    @Size(min = 2, max = 15, message = "비밀번호는 8~20자 이내로 입력하세요.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
    @Size(min = 2, max = 15, message = "비밀번호는 8~20자 이내로 입력하세요.")
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
