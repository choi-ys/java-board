package io.example.board.repository.rdb.post;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import io.example.board.config.p6spy.P6spyLogMessageFormatConfiguration;
import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.rdb.post.Post;
import io.example.board.utils.generator.MemberGenerator;
import io.example.board.utils.generator.PostGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

import javax.persistence.EntityManager;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오전 5:08
 */
@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration.class)
@Import({P6spyLogMessageFormatConfiguration.class, MemberGenerator.class, PostGenerator.class})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
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
                () -> assertEquals(expected.getMember(), post.getMember())
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
}