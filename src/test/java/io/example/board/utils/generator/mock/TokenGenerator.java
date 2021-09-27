package io.example.board.utils.generator.mock;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import io.example.board.config.security.jwt.common.ClaimKey;
import io.example.board.config.security.jwt.common.TokenType;
import io.example.board.config.security.jwt.provider.TokenProvider;
import io.example.board.config.security.jwt.verifier.VerifyResult;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.domain.vo.token.Token;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.TestConstructor;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : choi-ys
 * @date : 2021/09/23 2:44 오후
 */
@TestComponent
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(MemberGenerator.class)
// TODO: 용석(2021-09-27) : assertions saved object (Assert.notNull(id, "Entity must be saved")
public class TokenGenerator {

    private final MemberGenerator memberGenerator;
    private final TokenProvider tokenProvider;

    static final String SIGNATURE = "test-case-signature";
    static final long ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM = 600;

    public TokenGenerator(MemberGenerator memberGenerator, TokenProvider tokenProvider) {
        this.memberGenerator = memberGenerator;
        this.tokenProvider = tokenProvider;
    }

    public Token generateToken() {
        Member savedMember = memberGenerator.savedMember();
        LoginUserAdapter loginUserAdapter = new LoginUserAdapter(savedMember.getEmail(), savedMember.mapToSimpleGrantedAuthority());
        return tokenProvider.createToken(loginUserAdapter);
    }

    public Token generateToken(Member savedMember) {
        LoginUserAdapter loginUserAdapter = new LoginUserAdapter(savedMember.getEmail(), savedMember.mapToSimpleGrantedAuthority());
        return tokenProvider.createToken(loginUserAdapter);
    }

    public static String getBearerToken(String token) {
        return "Bearer " + token;
    }

    public static VerifyResult generateVerifyResult() {
        return VerifyResult.mapTo(getClaimsMap());
    }

    public static String generateJWT() {
        long currentTimeMillis = System.currentTimeMillis();
        LoginUserAdapter loginUserAdapter = MemberGenerator.loginUserAdapter();

        return JWT.create()
                .withIssuer("ISSUER")
                .withSubject("SUBJECT")
                .withAudience("AUDIENCE")
                .withIssuedAt(new Date(currentTimeMillis))
                .withExpiresAt(new Date(currentTimeMillis + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM * 1000))
                .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name())
                .withClaim(ClaimKey.USERNAME.value, loginUserAdapter.getUsername())
                .withClaim(ClaimKey.AUTHORITIES.value, loginUserAdapter.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .sign(Algorithm.HMAC256(SIGNATURE));
    }

    public static Token generateMockingToken() {
        return new Token(generateJWT(), generateJWT(), new Date(), new Date());
    }

    public static Map<String, Claim> getClaimsMap() {
        return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(generateJWT()).getClaims();
    }

    public static Map<String, Claim> getClaimsMap(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token).getClaims();
    }
}
