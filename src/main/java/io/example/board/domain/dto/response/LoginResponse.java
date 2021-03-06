package io.example.board.domain.dto.response;

import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.member.MemberRole;
import io.example.board.domain.vo.token.Token;
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

    private Token token;

    private LoginResponse(Long id, String email, String name, String nickname, Set<MemberRole> roles, Token token) {
        super(id, email, name, nickname);
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
