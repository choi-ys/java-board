package io.example.board.service;

import io.example.board.config.security.jwt.verifier.TokenVerifier;
import io.example.board.config.security.jwt.verifier.VerifyResult;
import io.example.board.domain.dto.request.LoginRequest;
import io.example.board.domain.dto.response.LoginResponse;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.domain.vo.token.Token;
import io.example.board.repository.rdb.member.MemberRepo;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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
    private final TokenVerifier tokenVerifier;
    private final TokenService tokenService;

    public LoginService(MemberRepo memberRepo, PasswordEncoder passwordEncoder,
                        TokenVerifier tokenVerifier, TokenService tokenService) {
        this.memberRepo = memberRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenVerifier = tokenVerifier;
        this.tokenService = tokenService;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepo.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new AuthenticationCredentialsNotFoundException(ErrorCode.AUTHENTICATION_CREDENTIALS_NOT_FOUND.message)
        );

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException(ErrorCode.AUTHENTICATION_CREDENTIALS_NOT_FOUND.message);
        }

        LoginUserAdapter principal = new LoginUserAdapter(member.getEmail(), member.mapToSimpleGrantedAuthority());
        Token token = tokenService.issued(principal);

        return LoginResponse.mapTo(member, token);
    }

    public Token refresh(String refreshToken) {
        VerifyResult refreshTokenVerifyResult = tokenVerifier.verify(refreshToken);
        Member member = memberRepo.findByEmail(refreshTokenVerifyResult.getUsername()).orElseThrow(
                () -> new BadCredentialsException(ErrorCode.BAD_CREDENTIALS.message)
        );
        return tokenService.issued(new LoginUserAdapter(member.getEmail(), member.mapToSimpleGrantedAuthority()));
    }
}
