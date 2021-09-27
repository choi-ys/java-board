package io.example.board.config.security;

import io.example.board.config.security.endpoint.AuthenticationEndpointByRoles;
import io.example.board.config.security.jwt.verifier.JwtConfigurer;
import io.example.board.config.security.jwt.verifier.TokenVerifier;
import io.example.board.domain.rdb.member.MemberRole;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
                            it.antMatchers(HttpMethod.GET, AuthenticationEndpointByRoles.NONE.patterns(HttpMethod.GET)).permitAll();
                            it.antMatchers(HttpMethod.POST, AuthenticationEndpointByRoles.NONE.patterns(HttpMethod.POST)).permitAll();

                            it.antMatchers(HttpMethod.POST, AuthenticationEndpointByRoles.MEMBER.patterns(HttpMethod.POST)).hasRole(MemberRole.MEMBER.name());
                            it.antMatchers(HttpMethod.PATCH, AuthenticationEndpointByRoles.MEMBER.patterns(HttpMethod.PATCH)).hasRole(MemberRole.MEMBER.name());
                            it.antMatchers(HttpMethod.DELETE, AuthenticationEndpointByRoles.MEMBER.patterns(HttpMethod.DELETE)).hasRole(MemberRole.MEMBER.name());

                            it.anyRequest().authenticated();
                        }
                )
        ;
    }

    // 용석(2021-09-28) : Spring REST Docs 설정 추가로 인한 Spring Boot static resource의 요청 허용 설정
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers("/docs/index.html");
    }
}
