package io.example.board.config.security.jwt.verifier;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author : choi-ys
 * @date : 2021/09/22 1:41 오전
 */
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenVerifier tokenVerifier;

    public JwtConfigurer(TokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterAt(
                new JwtFilter(tokenVerifier),
                BasicAuthenticationFilter.class
        );
    }
}
