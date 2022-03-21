package io.example.board.repository.rdb.comment;

import io.example.board.config.test.DataJpaTestConfig;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Comment;
import io.example.board.domain.rdb.post.Post;
import io.example.board.utils.generator.mock.CommentGenerator;
import io.example.board.utils.generator.mock.MemberGenerator;
import io.example.board.utils.generator.mock.PostGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Predicate.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2022/03/21 10:34 오전
 */
@DataJpaTestConfig
@Import({MemberGenerator.class, PostGenerator.class, CommentGenerator.class})
@DisplayName("Repo:Comment")
class CommentRepoTest {

    private final MemberGenerator memberGenerator;
    private final PostGenerator postGenerator;
    private final CommentGenerator commentGenerator;
    private final CommentRepo commentRepo;

    public CommentRepoTest(
            MemberGenerator memberGenerator,
            PostGenerator postGenerator,
            CommentGenerator commentGenerator,
            CommentRepo commentRepo
    ) {
        this.memberGenerator = memberGenerator;
        this.postGenerator = postGenerator;
        this.commentGenerator = commentGenerator;
        this.commentRepo = commentRepo;
    }

    @Test
    @DisplayName("게시글 댓글 저장")
    public void save() {
        // Given
        Post savedPost = postGenerator.savedPost(memberGenerator.savedMember());
        Member savedCommentWriter = memberGenerator.savedMember(MemberGenerator.secondMember());
        final Comment comment = CommentGenerator.commentMock(savedPost, savedCommentWriter);

        // When
        Comment expected = commentRepo.save(comment);

        // Then
        assertAll(
                () -> assertEquals(expected.content, CommentGenerator.COMMENT_CONTENT),
                () -> assertEquals(expected.getPost(), savedPost),
                () -> assertEquals(expected.getMember(), savedCommentWriter),
                () -> assertNotNull(expected.getCreatedAt()),
                () -> assertNotNull(expected.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("특정 게시글의 댓글 목록 조회")
    public void findByPostsComments() {
        // Given
        Post savedPost = postGenerator.savedPost(memberGenerator.savedMember());
        Member savedCommentWriter = memberGenerator.savedMember(MemberGenerator.secondMember());
        List<Comment> savedComments = commentGenerator.savedComments(savedPost, savedCommentWriter);

        // When
        List<Comment> expected = commentRepo.findByPost(savedPost);
        expected.stream()
                .map(Comment::getPost)
                .allMatch(isEqual(savedPost));

        // Then
        assertAll(
                () -> assertEquals(expected.size(), savedComments.size()),
                () -> assertTrue(
                        expected.stream()
                                .map(Comment::getPost)
                                .allMatch(isEqual(savedPost))
                )
        );
    }
}