package io.example.board.domain.vo.login;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author : choi-ys
 * @date : 2021/09/22 4:31 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginUser {
    private String email;
    Collection<? extends GrantedAuthority> authorities;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/22 4:32 오후
    // * --------------------------------------------------------------
    public LoginUser(String email, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.authorities = authorities;
    }
}
