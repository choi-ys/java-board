package io.example.board.config.security.jwt.provider;

import io.example.board.config.security.jwt.verifier.TokenVerifier;
import io.example.board.config.security.jwt.verifier.VerifyResult;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.member.MemberRole;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.domain.vo.token.Token;
import io.example.board.utils.generator.mock.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static io.example.board.utils.LocalDateTimeUtils.timestampToLocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/22 2:24 오전
 */
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@DisplayName("Component:TokenProvider")
class TokenProviderTest {

    private final TokenProvider tokenProvider;
    private final TokenVerifier tokenVerifier;

    public TokenProviderTest(TokenProvider tokenProvider, TokenVerifier tokenVerifier) {
        this.tokenProvider = tokenProvider;
        this.tokenVerifier = tokenVerifier;
    }

    @Test
    @DisplayName("Token 생성")
    public void createToken() {
        // Given
        Member member = MemberGenerator.member();
        member.addRoles(Set.of(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN));
        LoginUserAdapter loginUserAdapter = new LoginUserAdapter(member.getEmail(), member.mapToSimpleGrantedAuthority());

        // When
        Token createdToken = tokenProvider.createToken(loginUserAdapter);

        // Then
        VerifyResult accessTokenVerifyResult = tokenVerifier.verify(createdToken.getAccessToken());
        VerifyResult refreshTokenVerifyResult = tokenVerifier.verify(createdToken.getRefreshToken());
        LocalDateTime accessExpiredLocalDateTime = timestampToLocalDateTime(createdToken.getAccessExpired().getTime());
        LocalDateTime refreshExpiredLocalDateTime = timestampToLocalDateTime(createdToken.getRefreshExpired().getTime());
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        assertAll(
                () -> assertEquals(accessTokenVerifyResult.getUsername(), member.getEmail()),
                () -> assertEquals(refreshTokenVerifyResult.getUsername(), accessTokenVerifyResult.getUsername()),
                () -> assertTrue(accessTokenVerifyResult.getAuthorities().containsAll(member.mapToSimpleGrantedAuthority()), "Access Token의 payload에 Authority 포함 여부 확인"),
                () -> assertTrue(refreshTokenVerifyResult.getAuthorities().containsAll(member.mapToSimpleGrantedAuthority()), "Refresh Token의 payload에 Authority 포함 여부 확인"),
                () -> assertEquals(
                        LocalDateTime.now().plusMinutes(10).format(ofPattern),
                        accessExpiredLocalDateTime.format(ofPattern),
                        "발급된 접근 토큰의 유효기간이 10분인지 확인"
                ),
                () -> assertEquals(
                        LocalDateTime.now().plusMinutes(20).format(ofPattern),
                        refreshExpiredLocalDateTime.format(ofPattern),
                        "발급된 갱신 토큰의 유효기간이 20분인지 확인"
                )
        );
    }
}