package io.example.board.service;

import io.example.board.config.security.jwt.provider.TokenProvider;
import io.example.board.config.security.jwt.verifier.TokenVerifier;
import io.example.board.config.security.jwt.verifier.VerifyResult;
import io.example.board.domain.dto.request.LoginRequest;
import io.example.board.domain.dto.response.LoginResponse;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.member.Member;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.domain.vo.token.Token;
import io.example.board.repository.MemberRepo;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final TokenVerifier tokenVerifier;

    public LoginService(MemberRepo memberRepo, PasswordEncoder passwordEncoder,
                        TokenProvider tokenProvider, TokenVerifier tokenVerifier) {
        this.memberRepo = memberRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.tokenVerifier = tokenVerifier;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepo.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new BadCredentialsException(ErrorCode.BAD_CREDENTIALS.message)
        );

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new BadCredentialsException(ErrorCode.BAD_CREDENTIALS.message);
        }

        LoginUserAdapter principal = new LoginUserAdapter(member.getEmail(), member.mapToSimpleGrantedAuthority());
        Token token = tokenProvider.createToken(principal);

        return LoginResponse.mapTo(member, token);
    }

    public Token refresh(String refreshToken) {
        VerifyResult refreshTokenVerifyResult = tokenVerifier.verify(refreshToken);
        Member member = memberRepo.findByEmail(refreshTokenVerifyResult.getUsername()).orElseThrow(
                () -> new BadCredentialsException(ErrorCode.BAD_CREDENTIALS.message)
        );
        return tokenProvider.createToken(new LoginUserAdapter(member.getEmail(), member.mapToSimpleGrantedAuthority()));
    }
}
