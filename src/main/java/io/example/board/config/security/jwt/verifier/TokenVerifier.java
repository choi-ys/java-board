package io.example.board.config.security.jwt.verifier;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.utils.LocalDateTimeUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author : choi-ys
 * @date : 2021/09/21 11:34 오후
 */
@Component
public class TokenVerifier implements InitializingBean {
    @Value("${jwt.signature}")
    private String SIGNATURE;
    private Algorithm ALGORITHM;

    /**
     * TokenProvider Bean 생성 이후 application.yml의 jwt.signature_key값 로드 완료 후 ALGORITHM 초기화
     */
    @Override
    public void afterPropertiesSet() {
        ALGORITHM = Algorithm.HMAC256(SIGNATURE);
    }

    public String resolve(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION);
        if (!StringUtils.hasText(bearerToken)) {
            return "";
        } else {
            boolean isBearerToken = bearerToken.startsWith("Bearer ");
            if (isBearerToken) {
                return bearerToken.substring(7, bearerToken.length());
            } else {
                return "";
            }
        }
    }

    public VerifyResult verify(String token) {
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            Map<String, Claim> claims = verify.getClaims();
            return VerifyResult.mapTo(claims);
        } catch (TokenExpiredException e) { // 유효기간이 만료된 토큰
            DecodedJWT decode = JWT.decode(token);
            VerifyResult verifyResult = VerifyResult.mapTo(decode.getClaims());
            throw new TokenExpiredException("유효기간이 만료 되었습니다." + LocalDateTimeUtils.timestampToLocalDateTime(verifyResult.getExpiresAt()));
        } catch (JWTDecodeException e) { // 유효하지 못한 형식의 토큰
            throw new JWTDecodeException("잘못된 형식의 토큰입니다.");
        } catch (SignatureVerificationException e) { // 잘못된 서명을 가진 토큰
            throw new SignatureVerificationException(ALGORITHM, e.getCause());
        }
    }

    public Authentication getAuthentication(String token) {
        VerifyResult verifyResult = verify(token);
        LoginUserAdapter loginUserAdapter = new LoginUserAdapter(verifyResult.getUsername(), verifyResult.getAuthorities());
        return new UsernamePasswordAuthenticationToken(loginUserAdapter, null, loginUserAdapter.getAuthorities());
    }
}
