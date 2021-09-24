package io.example.board.service;

import io.example.board.config.security.jwt.provider.TokenProvider;
import io.example.board.config.security.jwt.verifier.TokenVerifier;
import io.example.board.domain.redis.token.BlackListCache;
import io.example.board.domain.redis.token.WhiteListCache;
import io.example.board.domain.vo.token.Token;
import io.example.board.repository.redis.token.BlackListCacheRepo;
import io.example.board.repository.redis.token.WhiteListCacheRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author : choi-ys
 * @date : 2021/09/24 11:38 오전
 */
@Service
@Slf4j
public class TokenService {

    private final TokenProvider tokenProvider;
    private final TokenVerifier tokenVerifier;
    private final WhiteListCacheRepo whiteListCacheRepo;
    private final BlackListCacheRepo blackListCacheRepo;

    public TokenService(TokenProvider tokenProvider, TokenVerifier tokenVerifier,
                        WhiteListCacheRepo whiteListCacheRepo, BlackListCacheRepo blackListCacheRepo) {
        this.tokenProvider = tokenProvider;
        this.tokenVerifier = tokenVerifier;
        this.whiteListCacheRepo = whiteListCacheRepo;
        this.blackListCacheRepo = blackListCacheRepo;
    }

    public Token issued(UserDetails principal) {
        Token token = tokenProvider.createToken(principal);
        /**
         * - 기 발급 토큰이 있는 경우, 만료 후 재 발급(블랙리스트)
         * - 기 발급 토큰이 없는 경우, 신규 발급
         */
        Optional<WhiteListCache> OptionalWhiteListCache = whiteListCacheRepo.findById(principal.getUsername());
        if (OptionalWhiteListCache.isPresent()) {
            WhiteListCache whiteListCache = OptionalWhiteListCache.get();
            blackListCacheRepo.save(new BlackListCache(whiteListCache.getId(), whiteListCache.getToken()));
        }

        whiteListCacheRepo.save(new WhiteListCache(principal.getUsername(), token));
        return token;
    }

}
