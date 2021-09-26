package io.example.board.domain.vo.login;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

/**
 * @author : choi-ys
 * @date : 2021/09/22 4:30 오후
 */
@Getter
public class LoginUserAdapter extends User {

    private LoginUser loginUser;

    public LoginUserAdapter(String username, Set<SimpleGrantedAuthority> authorities) {
        super(username, "", authorities);
        this.loginUser = new LoginUser(username, authorities);
    }
}
