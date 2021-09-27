package io.example.board.repository.redis.token;

import io.example.board.config.redis.EmbeddedRedisConfig;
import io.example.board.domain.redis.token.WhiteListCache;
import io.example.board.domain.vo.token.Token;
import io.example.board.utils.generator.mock.MemberGenerator;
import io.example.board.utils.generator.mock.TokenGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/24 1:21 오전
 */
@DataRedisTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("test")
@DisplayName("Repo:WhiteList")
class WhiteListCacheRepoTest {

    private final WhiteListCacheRepo whiteListCacheRepo;

    public WhiteListCacheRepoTest(WhiteListCacheRepo whiteListCacheRepo) {
        this.whiteListCacheRepo = whiteListCacheRepo;
    }

    private WhiteListCache savedWhiteListCache() {
        String email = MemberGenerator.email;
        Token token = TokenGenerator.generateMockingToken();
        WhiteListCache whiteListCache = new WhiteListCache(email, token);
        return whiteListCacheRepo.save(whiteListCache);
    }

    @Test
    @DisplayName("발급 토큰 객체 저장")
    public void save() {
        // Given
        String email = MemberGenerator.email;
        Token token = TokenGenerator.generateMockingToken();
        WhiteListCache whiteListCache = new WhiteListCache(email, token);

        // When
        WhiteListCache expected = whiteListCacheRepo.save(whiteListCache);

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), email),
                () -> assertEquals(expected.getToken(), token)
        );
    }

    @Test
    @DisplayName("[Hit]발급 토큰 객체 조회")
    public void findById() {
        // Given
        WhiteListCache savedWhiteListCache = savedWhiteListCache();

        // When
        WhiteListCache expected = whiteListCacheRepo.findById(savedWhiteListCache.getId()).orElseThrow();

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), savedWhiteListCache.getId()),
                () -> assertTrue(expected.getToken().equals(savedWhiteListCache.getToken()))
        );
    }

    @Test
    @DisplayName("[Fail]발급 토큰 객체 조회")
    public void findById_HitFail() {
        // Given
        String invalidKey = MemberGenerator.name;

        // When
        NoSuchElementException expected = assertThrows(NoSuchElementException.class,
                () -> whiteListCacheRepo.findById(invalidKey).orElseThrow());

        // Then
        assertAll(
                () -> assertEquals(expected.getClass().getSimpleName(), NoSuchElementException.class.getSimpleName()),
                () -> assertEquals(expected.getMessage(), "No value present")
        );
    }
}