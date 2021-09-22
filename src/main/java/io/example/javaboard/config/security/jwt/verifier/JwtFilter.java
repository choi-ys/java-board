package io.example.javaboard.config.security.jwt.verifier;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author : choi-ys
 * @date : 2021/09/21 11:36 오후
 */
public class JwtFilter extends GenericFilterBean {

    private final TokenVerifier tokenVerifier;

    public JwtFilter(TokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = tokenVerifier.resolve((HttpServletRequest) servletRequest);
        if ("" != token) {
            VerifyResult verifyResult = tokenVerifier.verify(token);
            if (verifyResult.isSuccess()) {
                //                SecurityContextHolder.getContext().setAuthentication();
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }
}
