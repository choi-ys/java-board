package io.example.board.repository.rdb.post;

import io.example.board.config.test.DataJpaTestConfig;
import io.example.board.domain.dto.request.SearchPostRequest;
import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.dto.response.SearchPostResponse;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Post;
import io.example.board.utils.generator.mock.MemberGenerator;
import io.example.board.utils.generator.mock.PostGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오전 5:08
 */
@DataJpaTestConfig
@Import({MemberGenerator.class, PostGenerator.class})
@DisplayName("Repo:Post")
class PostRepoTest {

    private final PostRepo postRepo;
    private final EntityManager entityManager;
    private final MemberGenerator memberGenerator;
    private final PostGenerator postGenerator;

    public PostRepoTest(PostRepo postRepo, EntityManager entityManager, MemberGenerator memberGenerator, PostGenerator postGenerator) {
        this.postRepo = postRepo;
        this.entityManager = entityManager;
        this.memberGenerator = memberGenerator;
        this.postGenerator = postGenerator;
    }

    public static final String title = "게시글 제목";
    public static final String content = "게시글 본문";

    @Test
    @DisplayName("게시글 객체 저장")
    public void save() {
        // Given
        Post post = postGenerator.post();

        // When
        Post expected = postRepo.save(post);
        entityManager.flush();

        // Then
        assertAll(
                () -> assertNotNull(expected.getId()),
                () -> assertEquals(expected.getTitle(), post.getTitle()),
                () -> assertEquals(expected.getContent(), post.getContent()),
                () -> assertEquals(expected.getMember(), post.getMember()),
                () -> assertNotNull(expected.getCreatedAt(), "Auditor를 통해 설정되는 생성일자 정보의 null 여부를 확인"),
                () -> assertNotNull(expected.getUpdatedAt(), "Auditor를 통해 설정되는 수정일자 정보의 null 여부를 확인")
        );
    }

    @Test
    @DisplayName("게시글 조회")
    public void findById() {
        // Given
        Post savedPost = postGenerator.savedPost();
        entityManager.clear();

        // When
        Post expected = postRepo.findById(savedPost.getId()).orElseThrow();

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), savedPost.getId()),
                () -> assertNotNull(expected.getMember())
        );
    }

    @Test
    @DisplayName("나의 게시글 상세 조회")
    public void findByIdAndMemberEmail() {
        // Given
        Member savedMember = memberGenerator.savedMember();
        Post savedPost = postGenerator.savedPost(savedMember);
        entityManager.clear();

        // When
        Post expected = postRepo.findByIdAndMemberEmail(savedPost.getId(), savedMember.getEmail()).orElseThrow();

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), savedPost.getId()),
                () -> assertNotNull(expected.getMember())
        );
    }

    @Test
    @DisplayName("나의 게시글 상세 조회 실패(게시자가 아닌 사용자의 요청)")
    public void findByIdAndMemberEmail_Fail_Cause_BadCredentials() {
        // Given
        String notPostedMemberEmail = "choi.ys@naver.com";
        Post savedPost = postGenerator.savedPost();
        entityManager.clear();

        // When
        RuntimeException expected = assertThrows(
                RuntimeException.class,
                () -> postRepo.findByIdAndMemberEmail(savedPost.getId(), notPostedMemberEmail).orElseThrow()
        );

        // Then
        assertTrue(expected instanceof NoSuchElementException);
    }

    @Test
    @DisplayName("전시 상태의 게시글 조회")
    public void findByIdAndDisplay() {
        // Given
        Post savedPost = postGenerator.savedPost();
        entityManager.clear();

        // When
        Post expected = postRepo.findByIdAndDisplayTrue(savedPost.getId()).orElseThrow();

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), savedPost.getId()),
                () -> assertNotNull(expected.getMember())
        );
    }

    @Test
    @DisplayName("전시 상태의 게시글 조회 실패(미전시 게시글 조회 요청)")
    public void findByIdAndDisplay_Fail_CauseNotDisplayedResource() {
        // Given
        Post savedPost = postGenerator.savedPost();
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                false
        );
        savedPost.update(postUpdateRequest);
        entityManager.flush();
        entityManager.clear();

        // When
        RuntimeException expected = assertThrows(NoSuchElementException.class,
                () -> postRepo.findByIdAndDisplayTrue(savedPost.getId()).orElseThrow()
        );

        // Then
        assertTrue(expected instanceof NoSuchElementException);
    }

    @Test
    @DisplayName("게시글 수정")
    public void updateByDirtyChecking() {
        // Given
        Post savedPost = postGenerator.savedPost();
        String newTitle = "수정된 제목";
        String newContent = "수정된 본문";
        boolean changedDisplay = false;
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(0L, newTitle, newContent, changedDisplay);

        // When
        savedPost.update(postUpdateRequest);
        entityManager.flush();
        entityManager.clear();

        // Then
        Post expected = postRepo.findById(savedPost.getId()).orElseThrow();

        assertAll(
                () -> assertEquals(expected.getTitle(), newTitle),
                () -> assertEquals(expected.getContent(), newContent),
                () -> assertEquals(expected.isDisplay(), changedDisplay)
        );
    }

    @Test
    @DisplayName("게시글 객체 삭제")
    public void delete() {
        // Given
        Post savedPost = postGenerator.savedPost();

        // When
        postRepo.delete(savedPost);
        entityManager.flush();

        // Then
        assertThrows(NoSuchElementException.class, () -> postRepo.findById(savedPost.getId()).orElseThrow());
    }

    @Test
    @DisplayName("게시글 검색")
    public void searchPost() {
        // Given
        postGenerator.savedPost();
        entityManager.flush();

        String title = "제목";
        String content = "본문";
        String writerName = "choi-ys";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1L);
        LocalDateTime updatedAt = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);

        SearchPostRequest searchPostRequest = new SearchPostRequest(
                title, content, writerName, createdAt, updatedAt, pageable
        );

        // When
        Page<SearchPostResponse> expected = postRepo.findPostPageBySearchParams(searchPostRequest);

        // Then
        assertThat(expected)
                .hasSize(1)
                .allSatisfy(postSearchResponse -> {
                    assertTrue(postSearchResponse.getTitle().contains(searchPostRequest.getTitle()));
                    assertTrue(postSearchResponse.getContent().contains(searchPostRequest.getContent()));
                    assertTrue(postSearchResponse.getWriter().getName().equals(searchPostRequest.getWriterName()));
                    assertThat(postSearchResponse.getCreatedAt()).isAfterOrEqualTo(searchPostRequest.getCreatedAt());
                    assertThat(postSearchResponse.getCreatedAt()).isBeforeOrEqualTo(searchPostRequest.getUpdatedAt());
                });
    }

    @Test
    @DisplayName("게시글 검색: 검색 조건이 없는 경우")
    public void givenNullSearchParams_whenFindPostPage_thenReturnSearchedFirstPostPage() {
        // Given
        postGenerator.savedPost();
        entityManager.flush();

        SearchPostRequest searchPostRequest = new SearchPostRequest(
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 5)
        );

        // When
        Page<SearchPostResponse> expected = postRepo.findPostPageBySearchParams(searchPostRequest);

        // Then
        assertThat(expected).hasSize(1);
    }

    @Test
    @DisplayName("게시글 검색: 검색조건과 페이지 요청 정보가 없는 경우")
    public void givenNullSearchParamsAndPageRequest_whenFindPostPage_thenReturnSearchedFirstPostPage() {
        // Given
        postGenerator.savedPost();
        entityManager.flush();

        SearchPostRequest searchPostRequest = new SearchPostRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );

        // When
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> postRepo.findPostPageBySearchParams(searchPostRequest));

        // Then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class);
    }
}