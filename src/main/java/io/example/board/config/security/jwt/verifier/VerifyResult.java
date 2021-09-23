package io.example.board.config.security.jwt.verifier;

import com.auth0.jwt.interfaces.Claim;
import io.example.board.config.security.jwt.common.ClaimKey;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : choi-ys
 * @date : 2021/09/21 11:36 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerifyResult {
    private String issuer;
    private String subject;
    private String audience;
    private Long issuedAt;
    private Long expiresAt;
    private String use;
    private String username;
    private Set<SimpleGrantedAuthority> authorities;

    private VerifyResult(String issuer, String subject, String audience, Long issuedAt, Long expiresAt, String use, String username, Set<SimpleGrantedAuthority> authorities) {
        this.issuer = issuer;
        this.subject = subject;
        this.audience = audience;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.use = use;
        this.username = username;
        this.authorities = authorities;
    }

    public static VerifyResult mapTo(Map<String, Claim> claims) {
        return new VerifyResult(
                claims.get(ClaimKey.ISS.value).asString(),
                claims.get(ClaimKey.SUB.value).asString(),
                claims.get(ClaimKey.AUD.value).asString(),
                Long.parseLong(String.valueOf(claims.get(ClaimKey.IAT.value))),
                Long.parseLong(String.valueOf(claims.get(ClaimKey.EXP.value))),
                claims.get(ClaimKey.USE.value).asString(),
                claims.get(ClaimKey.USERNAME.value).asString(),
                Arrays.stream(claims.get(ClaimKey.AUTHORITIES.value).asString().split(","))
                        .map(it -> new SimpleGrantedAuthority(it))
                        .collect(Collectors.toSet())
        );
    }
}
