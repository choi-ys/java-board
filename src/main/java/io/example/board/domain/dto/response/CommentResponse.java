package io.example.board.domain.dto.response;

import io.example.board.domain.rdb.post.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author : choi-ys
 * @date : 2022/03/23 2:43 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {
    private Long id;
    private Long postId;
    private String content;
    private MemberSimpleResponse writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResponse(Long id, Long postId, String content, MemberSimpleResponse writer, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentResponse mapTo(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                MemberSimpleResponse.mapTo(comment.getMember()),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
