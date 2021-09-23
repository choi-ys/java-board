package io.example.board.config.security.jwt.verifier;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.example.board.config.security.jwt.common.ClaimKey;
import io.example.board.config.security.jwt.common.TokenType;
import io.example.board.config.security.jwt.provider.TokenProvider;
import io.example.board.domain.member.Member;
import io.example.board.domain.member.MemberRole;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.domain.vo.token.Token;
import io.example.board.utils.generator.MemberGenerator;
import io.example.board.utils.generator.TokenGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static io.example.board.utils.LocalDateTimeUtils.timestampToLocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/22 8:19 오후
 */
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(TokenGenerator.class)
@ActiveProfiles("test")
@DisplayName("Component:TokenVerifier")
class TokenVerifierTest {

    private final TokenProvider tokenProvider;
    private final TokenVerifier tokenVerifier;
    private final TokenGenerator tokenGenerator;

    public TokenVerifierTest(TokenProvider tokenProvider, TokenVerifier tokenVerifier, TokenGenerator tokenGenerator) {
        this.tokenProvider = tokenProvider;
        this.tokenVerifier = tokenVerifier;
        this.tokenGenerator = tokenGenerator;
    }

    @Test
    @DisplayName("정상 JWT 검증")
    public void verify() {
        // Given
        Member member = MemberGenerator.member();
        Token createdToken = tokenGenerator.generateToken(member);

        // When
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

    @Test
    @DisplayName("JWT 형식이 아닌 토큰 검증")
    public void verity_Fail_CauseInvalidJwtFormat() {
        // Given
        String invalidJsonFormat = "aaa";

        // When
        JWTDecodeException expected = assertThrows(JWTDecodeException.class,
                () -> tokenVerifier.verify(invalidJsonFormat)
        );

        // Then
        assertAll(
                () -> assertEquals(expected.getClass().getSimpleName(), JWTDecodeException.class.getSimpleName()),
                () -> assertEquals(expected.getMessage(), "잘못된 형식의 토큰입니다.")
        );
    }

    @Test
    @DisplayName("JSON 형식이 아닌 토큰 검증")
    public void verity_Fail_CauseInvalidJsonFormat() {
        // Given
        String inValidJsonFormat = "header.payload.signature";

        // When
        JWTDecodeException expected = assertThrows(JWTDecodeException.class,
                () -> tokenVerifier.verify(inValidJsonFormat)
        );

        // Then
        assertAll(
                () -> assertEquals(expected.getClass().getSimpleName(), JWTDecodeException.class.getSimpleName()),
                () -> assertEquals(expected.getMessage(), "잘못된 형식의 토큰입니다.")
        );
    }

    @Test
    @DisplayName("잘못된 서명을 가진 토큰 검증")
    public void verify_Fail_CauseUnsupportedJwt() {
        // Given
        long currentTimeMillis = System.currentTimeMillis();
        long ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM = 1000L;
        String SIGNATURE = "test-signature";

        LoginUserAdapter loginUserAdapter = new LoginUserAdapter(
                "project.log.062@gmail.com",
                Set.of(new SimpleGrantedAuthority("ROLE_" + MemberRole.MEMBER))
        );

        // When
        String token = JWT.create()
                .withIssuer("ISSUER")
                .withSubject("SUBJECT")
                .withAudience("AUDIENCE")
                .withIssuedAt(new Date(currentTimeMillis))
                .withExpiresAt(new Date(currentTimeMillis + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM * 1000))
                .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name())
                .withClaim(ClaimKey.USERNAME.value, loginUserAdapter.getUsername())
                .withClaim(ClaimKey.AUTHORITIES.value, loginUserAdapter.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .sign(Algorithm.HMAC256(SIGNATURE));

        // Then
        Exception expected = assertThrows(SignatureVerificationException.class,
                () -> tokenVerifier.verify(token)
        );

        assertAll(
                () -> assertEquals(expected.getClass().getSimpleName(), SignatureVerificationException.class.getSimpleName()),
                () -> assertTrue(expected.getMessage().contains("The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256"))
        );
    }

    @Value("${jwt.signature}")
    private String SIGNATURE;

    @Test
    @DisplayName("유효기간이 만료된 토큰 검증")
    public void verify_Fail_CauseExpiredJwt() throws InterruptedException {
        // Given
        long currentTimeMillis = System.currentTimeMillis();
        long ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM = 1L;

        LoginUserAdapter loginUserAdapter = new LoginUserAdapter(
                "project.log.062@gmail.com",
                Set.of(new SimpleGrantedAuthority("ROLE_" + MemberRole.MEMBER))
        );

        // When
        String token = JWT.create()
                .withIssuer("ISSUER")
                .withSubject("SUBJECT")
                .withAudience("AUDIENCE")
                .withIssuedAt(new Date(currentTimeMillis))
                .withExpiresAt(new Date(currentTimeMillis + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM))
                .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name())
                .withClaim(ClaimKey.USERNAME.value, loginUserAdapter.getUsername())
                .withClaim(ClaimKey.AUTHORITIES.value, loginUserAdapter.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .sign(Algorithm.HMAC256(SIGNATURE));

        // Then
        Thread.sleep(1000L);
        Exception expected = assertThrows(TokenExpiredException.class,
                () -> tokenVerifier.verify(token)
        );

        assertAll(
                () -> assertEquals(expected.getClass().getSimpleName(), TokenExpiredException.class.getSimpleName()),
                () -> assertTrue(expected.getMessage().contains("유효기간이 만료 되었습니다."))
        );
    }

    @Test
    @DisplayName("인증된 사용자 정보 조회")
    public void getAuthentication() {
        // Given
        Member member = MemberGenerator.member();

        Token token = tokenProvider.createToken(new LoginUserAdapter(member.getEmail(), member.mapToSimpleGrantedAuthority()));

        // When
        Authentication authentication = tokenVerifier.getAuthentication(token.getAccessToken());
        LoginUserAdapter loginUserAdapter = (LoginUserAdapter) authentication.getPrincipal();

        // Then
        assertAll(
                () -> assertEquals(loginUserAdapter.getUsername(), member.getEmail()),
                () -> assertEquals(loginUserAdapter.getAuthorities(), member.mapToSimpleGrantedAuthority())
        );
    }
}