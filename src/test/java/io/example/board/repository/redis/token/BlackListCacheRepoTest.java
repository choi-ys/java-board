package io.example.board.repository.redis.token;

import io.example.board.config.redis.EmbeddedRedisConfig;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.redis.token.BlackListCache;
import io.example.board.domain.vo.token.Token;
import io.example.board.utils.generator.MemberGenerator;
import io.example.board.utils.generator.TokenGenerator;
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
 * @date : 2021/09/24 3:54 오후
 */
@DataRedisTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("test")
@DisplayName("Repo:BlackList")
class BlackListCacheRepoTest {

    private final BlackListCacheRepo blackListCacheRepo;

    public BlackListCacheRepoTest(BlackListCacheRepo blackListCacheRepo) {
        this.blackListCacheRepo = blackListCacheRepo;
    }

    private BlackListCache savedBlackListCache() {
        Member member = MemberGenerator.member();
        Token token = TokenGenerator.generateMockingToken();
        BlackListCache blackListCache = new BlackListCache(member.getEmail(), token);
        return blackListCacheRepo.save(blackListCache);
    }

    @Test
    @DisplayName("만료 대상 토큰 객체 저장")
    public void save() {
        // Given
        Member member = MemberGenerator.member();
        Token token = TokenGenerator.generateMockingToken();
        BlackListCache blackListCache = new BlackListCache(member.getEmail(), token);

        // When
        BlackListCache expected = blackListCacheRepo.save(blackListCache);

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), member.getEmail()),
                () -> assertTrue(expected.getToken().equals(token))
        );
    }

    @Test
    @DisplayName("[Hit]만료 토큰 조회")
    public void findById() {
        // Given
        BlackListCache savedBlackListCache = savedBlackListCache();

        // When
        BlackListCache expected = blackListCacheRepo.findById(MemberGenerator.email).orElseThrow();

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), savedBlackListCache.getId()),
                () -> assertTrue(expected.getToken().equals(savedBlackListCache.getToken()))
        );
    }

    @Test
    @DisplayName("[Fail]만료 토큰 조회")
    public void findById_HitFail() {
        // Given
        String invalidKey = MemberGenerator.name;

        // When
        NoSuchElementException expected = assertThrows(NoSuchElementException.class,
                () -> blackListCacheRepo.findById(invalidKey).orElseThrow());

        // Then
        assertAll(
                () -> assertEquals(expected.getClass().getSimpleName(), NoSuchElementException.class.getSimpleName()),
                () -> assertEquals(expected.getMessage(), "No value present")
        );
    }
}
