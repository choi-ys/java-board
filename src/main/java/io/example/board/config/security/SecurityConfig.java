package io.example.board.config.security;

import io.example.board.config.security.jwt.verifier.JwtConfigurer;
import io.example.board.config.security.jwt.verifier.TokenVerifier;
import io.example.board.domain.member.MemberRole;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author : choi-ys
 * @date : 2021/09/21 4:09 오전
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenVerifier tokenVerifier;

    public SecurityConfig(TokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .apply(new JwtConfigurer(tokenVerifier))
                .and()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests(
                        it -> {
                            it.antMatchers(HttpMethod.POST, "/member").permitAll();
                            it.antMatchers(HttpMethod.POST, "/login").permitAll();
                            it.antMatchers(HttpMethod.POST, "/refresh").hasRole(MemberRole.MEMBER.name());
                            it.anyRequest().authenticated();
                        }
                )
        ;
    }
}
