package io.example.board.config.jpa;

import io.example.board.domain.vo.login.LoginUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오전 8:58
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class DataJpaAuditorConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isValidAuditor(authentication)) {
//            return () -> Optional.empty();
            return () -> Optional.of(UUID.randomUUID().toString());
        } else {
            /**
             * TODO: 용석(2021-09-27) DataJpaAuditor 설정이,
             * JWT 토큰 검증 후 SecurityContextHolder의 Authentication 설정 보다 먼저 동작하여
             * principal을 가져올 수 없는 이슈
             */
            return () -> Optional.of(((LoginUser) authentication.getPrincipal()).getEmail());
        }
    }

    private Boolean isValidAuditor(Authentication authentication) {
        return authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken;
    }
}
