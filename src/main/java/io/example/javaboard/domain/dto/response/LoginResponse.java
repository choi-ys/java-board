package io.example.javaboard.domain.dto.response;

import io.example.javaboard.domain.member.Member;
import io.example.javaboard.domain.member.MemberRole;
import io.example.javaboard.domain.vo.token.Token;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author : choi-ys
 * @date : 2021/09/22 2:59 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse extends MemberSimpleResponse {

    private Set<MemberRole> roles;
    private Token token;

    public LoginResponse(Long id, String email, String name, String nickname, Set<MemberRole> roles, Token token) {
        super(id, email, name, nickname);
        this.roles = roles;
        this.token = token;
    }

    public static LoginResponse mapTo(Member member, Token token) {
        return new LoginResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getRoles(),
                token
        );
    }
}
