package io.example.javaboard.service;

import io.example.javaboard.domain.dto.request.LoginRequest;
import io.example.javaboard.domain.dto.response.LoginResponse;
import io.example.javaboard.domain.member.Member;
import io.example.javaboard.domain.vo.login.LoginUserAdapter;
import io.example.javaboard.repository.MemberRepo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : choi-ys
 * @date : 2021/09/22 2:27 오전
 */
@Service
@Transactional(readOnly = true)
public class LoginService {

    private final MemberRepo memberRepo;
    private final PasswordEncoder passwordEncoder;

    public LoginService(MemberRepo memberRepo, PasswordEncoder passwordEncoder) {
        this.memberRepo = memberRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        final String loginFailMessage = "요청에 해당하는 사용자 정보를 찾을 수 없습니다.";
        Member member = memberRepo.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException(loginFailMessage)
        );

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException(loginFailMessage);
        }

        LoginUserAdapter principal = new LoginUserAdapter(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // TODO: 용석(2021/09/22): Add created JWT to LoginResponse
        return LoginResponse.mapTo(member);
    }
}
