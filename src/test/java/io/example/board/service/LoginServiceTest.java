package io.example.board.service;

import io.example.board.config.security.jwt.provider.TokenProvider;
import io.example.board.config.security.jwt.verifier.TokenVerifier;
import io.example.board.domain.dto.request.LoginRequest;
import io.example.board.domain.dto.response.LoginResponse;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.domain.vo.token.Token;
import io.example.board.repository.rdb.member.MemberRepo;
import io.example.board.utils.generator.mock.MemberGenerator;
import io.example.board.utils.generator.mock.TokenGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author : choi-ys
 * @date : 2021/09/22 2:38 오전
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Login")
class LoginServiceTest {

    @Mock
    private MemberRepo memberRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private TokenVerifier tokenVerifier;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("로그인")
    public void login() {
        // Given
        Member member = MemberGenerator.member();

        given(memberRepo.findByEmail(anyString())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        LoginRequest loginRequest = new LoginRequest(member.getEmail(), member.getPassword());

        // When
        LoginResponse loginResponse = loginService.login(loginRequest);

        // Then
        verify(memberRepo, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(tokenService, times(1)).issued(any(LoginUserAdapter.class));

        assertAll(
                () -> assertEquals(loginResponse.getEmail(), member.getEmail()),
                () -> assertEquals(loginResponse.getName(), member.getName()),
                () -> assertEquals(loginResponse.getNickname(), member.getNickname())
        );
    }

    @Test
    @DisplayName("토큰 갱신")
    public void refresh() {
        // Given
        Member member = MemberGenerator.member();
        Token token = TokenGenerator.generateMockingToken();

        given(tokenVerifier.verify(anyString())).willReturn(TokenGenerator.generateVerifyResult());
        given(memberRepo.findByEmail(anyString())).willReturn(Optional.of(member));
        given(tokenService.issued(any(LoginUserAdapter.class))).willReturn(token);

        // When
        Token refreshedToken = loginService.refresh(token.getRefreshToken());

        // Then
        verify(tokenVerifier, times(1)).verify(anyString());
        verify(memberRepo, times(1)).findByEmail(anyString());
        verify(tokenService, times(1)).issued(any(LoginUserAdapter.class));

        assertAll(
                () -> assertNotNull(refreshedToken.getAccessToken()),
                () -> assertNotNull(refreshedToken.getRefreshToken()),
                () -> assertNotNull(refreshedToken.getAccessExpired()),
                () -> assertNotNull(refreshedToken.getRefreshExpired())
        );
    }
}