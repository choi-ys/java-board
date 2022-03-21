package io.example.board.repository.rdb.post;


import io.example.board.config.test.DataJpaTestConfig;
import io.example.board.domain.dto.request.SearchPostRequest;
import io.example.board.domain.dto.response.SearchPostResponse;
import io.example.board.domain.dto.response.SearchPostWithCommentsResponse;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Comment;
import io.example.board.domain.rdb.post.Post;
import io.example.board.utils.generator.mock.CommentGenerator;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2022/03/21 10:58 오전
 */
@DataJpaTestConfig
@Import({MemberGenerator.class, PostGenerator.class, CommentGenerator.class})
@DisplayName("Repo:PostQuery")
class PostQueryRepoImplTest {

    private final PostRepo postRepo;
    private final EntityManager entityManager;
    private final MemberGenerator memberGenerator;
    private final PostGenerator postGenerator;
    private final CommentGenerator commentGenerator;

    public PostQueryRepoImplTest(
            PostRepo postRepo,
            EntityManager entityManager,
            MemberGenerator memberGenerator,
            PostGenerator postGenerator,
            CommentGenerator commentGenerator
    ) {
        this.postRepo = postRepo;
        this.entityManager = entityManager;
        this.memberGenerator = memberGenerator;
        this.postGenerator = postGenerator;
        this.commentGenerator = commentGenerator;
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
                    assertEquals(searchPostRequest.getWriterName(), postSearchResponse.getWriter().getName());
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

    @Test
    @DisplayName("게시글 검색 시 게시글의 댓글 포함")
    public void givenPostAndComment_whenSearchPost_thenReturnPostAndCommentsOnPosts() {
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
}