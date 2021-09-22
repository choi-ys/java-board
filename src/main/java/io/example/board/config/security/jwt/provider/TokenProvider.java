package io.example.board.config.security.jwt.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.example.board.config.security.jwt.common.ClaimKey;
import io.example.board.config.security.jwt.common.TokenType;
import io.example.board.domain.vo.token.Token;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author : choi-ys
 * @date : 2021/09/21 11:33 오후
 */
@Component
public class TokenProvider implements InitializingBean {
    @Value("${jwt.signature}")
    private String SIGNATURE;
    private Algorithm ALGORITHM;

    @Value("${jwt.issuer}")
    private String ISSUER;

    @Value("${jwt.subject}")
    private final String SUBJECT = "resource-access";

    @Value("${jwt.audience}")
    private final String AUDIENCE = "client-server";

    @Value("${jwt.access-token-validity-in-seconds-term}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM;

    @Value("${jwt.refresh-token-validity-in-seconds-term}")
    private Long REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM;

    /**
     * TokenProvider Bean 생성 이후 application.yml의 jwt.signature_key값 로드 완료 후 ALGORITHM 초기화
     */
    @Override
    public void afterPropertiesSet() {
        ALGORITHM = Algorithm.HMAC256(SIGNATURE);
    }

    public Token createToken(UserDetails userDetails) {
        long currentTimeMillis = System.currentTimeMillis();
        Date accessExpired = new Date(System.currentTimeMillis() + (ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM * 1000));
        Date refreshExpired = new Date(System.currentTimeMillis() + (REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM * 1000));

        String accessToken = tokenBuilder(currentTimeMillis, TokenType.ACCESS, userDetails);
        String refreshToken = tokenBuilder(currentTimeMillis, TokenType.REFRESH, userDetails);

        return new Token(accessToken, refreshToken, accessExpired, refreshExpired);
    }

    private String tokenBuilder(long currentTimeMillis, TokenType tokenType, UserDetails userDetails) {
        long tokenValidityInSecondsTerm = tokenType.equals(TokenType.ACCESS) ?
                ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM : REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM;
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(SUBJECT)
                .withAudience(AUDIENCE)
                .withIssuedAt(new Date(currentTimeMillis))
                .withExpiresAt(new Date(currentTimeMillis + (tokenValidityInSecondsTerm * 1000)))
                .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name())
                .withClaim(ClaimKey.USERNAME.value, userDetails.getUsername())
                .withClaim(ClaimKey.AUTHORITIES.value, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .sign(ALGORITHM)
                ;
    }
}
