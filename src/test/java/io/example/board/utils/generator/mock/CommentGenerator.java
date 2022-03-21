package io.example.board.utils.generator.mock;

import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Comment;
import io.example.board.domain.rdb.post.Post;
import io.example.board.repository.rdb.comment.CommentRepo;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.TestConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2022/03/21 11:12 오전
 */
@TestComponent
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CommentGenerator {

    private final CommentRepo commentRepo;

    public static final String COMMENT_CONTENT = "댓글 내용";
    private final int DEFAULT_COMMENTS_GENERATOR_SIZE = 10;

    public CommentGenerator(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public static Comment commentMock(Post savedPost, Member savedCommentWriter) {
        return new Comment(COMMENT_CONTENT, savedPost, savedCommentWriter);
    }

    public Comment savedComment(Post savedPost, Member savedCommentWriter) {
        return commentRepo.saveAndFlush(commentMock(savedPost, savedCommentWriter));
    }

    public List<Comment> savedComments(Post savedPost, Member savedCommentWriter) {
        return commentRepo.saveAllAndFlush(
                generateComments(
                        savedPost,
                        savedCommentWriter,
                        DEFAULT_COMMENTS_GENERATOR_SIZE)
        );
    }

    public List<Comment> savedComments(Post savedPost, Member savedCommentWriter, int count) {
        return commentRepo.saveAllAndFlush(generateComments(savedPost, savedCommentWriter, count));
    }

    public List<Comment> generateComments(Post savedPost, Member savedCommentWriter, int count) {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comments.add(
                    new Comment(COMMENT_CONTENT.concat(":".concat(String.valueOf(count))),
                            savedPost,
                            savedCommentWriter)
            );
        }
        return comments;
    }
}
