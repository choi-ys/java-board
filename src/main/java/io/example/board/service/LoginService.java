package io.example.board.service;

import io.example.board.config.security.jwt.provider.TokenProvider;
import io.example.board.domain.dto.request.LoginRequest;
import io.example.board.domain.dto.response.LoginResponse;
import io.example.board.domain.member.Member;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.domain.vo.token.Token;
import io.example.board.repository.MemberRepo;
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
    private final TokenProvider tokenProvider;

    public LoginService(MemberRepo memberRepo, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.memberRepo = memberRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        final String loginFailMessage = "요청에 해당하는 사용자 정보를 찾을 수 없습니다.";
        Member member = memberRepo.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new IllegalArgumentException(loginFailMessage)
        );

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException(loginFailMessage);
        }

        LoginUserAdapter principal = new LoginUserAdapter(member.getEmail(), member.mapToSimpleGrantedAuthority());
        Token token = tokenProvider.createToken(principal);

        return LoginResponse.mapTo(member, token);
    }
}
