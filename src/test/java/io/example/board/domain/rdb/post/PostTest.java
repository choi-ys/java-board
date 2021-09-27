package io.example.board.domain.rdb.post;

import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.rdb.member.Member;
import io.example.board.utils.generator.mock.MemberGenerator;
import io.example.board.utils.generator.mock.PostGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오전 4:00
 */
@DisplayName("Entity:Post")
class PostTest {

    @Test
    @DisplayName("게시글 객체 생성")
    public void create() {
        // Given
        String title = "게시글 제목";
        String content = "게시글 본문";
        Member member = MemberGenerator.member();

        // When
        Post post = new Post(title, content, member);

        // Then
        assertAll(
                () -> assertEquals(post.getTitle(), title),
                () -> assertEquals(post.getContent(), content),
                () -> assertEquals(post.getMember(), member),
                () -> assertNull(post.getId()),
                () -> assertTrue(post.isDisplay()),
                () -> assertEquals(post.getViewCount(), 0L),
                () -> assertEquals(post.getCreatedBy(), null, "Auditor를 통해 설정되는 생성주체 정보의 null 여부를 확인"),
                () -> assertEquals(post.getCreatedAt(), null, "Auditor를 통해 설정되는 생성일자 정보의 null 여부를 확인"),
                () -> assertEquals(post.getUpdatedBy(), null, "Auditor를 통해 설정되는 수정주체 정보의 null 여부를 확인"),
                () -> assertEquals(post.getUpdatedAt(), null, "Auditor를 통해 설정되는 수정일자 정보의 null 여부를 확인")
        );
    }

    @Test
    @DisplayName("게시글 객체 수정")
    public void update() {
        // Given
        Post post = PostGenerator.postMock();
        PostUpdateRequest postUpdateRequest = PostGenerator.postUpdateRequestMock();

        // When
        post.update(postUpdateRequest);

        // Then
        assertAll(
                () -> assertEquals(post.getTitle(), postUpdateRequest.getTitle()),
                () -> assertEquals(post.getContent(), postUpdateRequest.getContent()),
                () -> assertEquals(post.isDisplay(), postUpdateRequest.isDisplay()),
                () -> assertFalse(post.isDeleted())
        );
    }

    @Test
    @DisplayName("게시글 객체 삭제")
    public void delete() {
        // Given
        Post post = PostGenerator.postMock();

        // When
        post.delete();

        // Then
        assertTrue(post.isDeleted());
    }
}