package io.example.board.domain.rdb.base;

import io.example.board.domain.vo.login.LoginUserAdapter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오후 11:30
 */
public class AuditorWAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isValidAuditor(authentication)) {
            return Optional.of(((LoginUserAdapter) authentication.getPrincipal()).getUsername());
        }
        return Optional.empty();
    }

    private Boolean isValidAuditor(Authentication authentication) {
        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }
}
