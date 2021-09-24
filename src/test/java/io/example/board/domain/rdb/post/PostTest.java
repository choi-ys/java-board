package io.example.board.domain.rdb.post;

import io.example.board.domain.rdb.member.Member;
import io.example.board.utils.generator.MemberGenerator;
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
    public void create(){
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
                () -> assertEquals(post.getViewCount(), 0L)
        );
    }
}