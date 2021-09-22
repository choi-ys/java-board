package io.example.javaboard.domain.vo.login;

import io.example.javaboard.domain.member.Member;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

/**
 * @author : choi-ys
 * @date : 2021/09/22 4:30 오후
 */
@Getter
public class LoginUserAdapter extends User {

    private LoginUser loginUser;

    public LoginUserAdapter(Member member) {
        super(member.getEmail(),
                "",
                member.isEnabled(),
                member.isEnabled(),
                member.isEnabled(),
                member.isEnabled(),
                member.mapToSimpleGrantedAuthority()
        );
        this.loginUser = new LoginUser(member.getEmail(), member.mapToSimpleGrantedAuthority());
    }
}
