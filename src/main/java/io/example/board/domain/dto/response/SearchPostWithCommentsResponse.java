package io.example.board.domain.dto.response;

import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Comment;
import io.example.board.domain.rdb.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : choi-ys
 * @date : 2021/12/13 4:54 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchPostWithCommentsResponse {
    private Long id;
    private String title;
    private String content;
    private long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MemberSimpleResponse writer;
    private List<CommentResponse> comments;

    public SearchPostWithCommentsResponse(
            Post post,
            Member member,
            List<Comment> comments
    ) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.writer = MemberSimpleResponse.mapTo(member);
        this.comments = comments.stream()
                .map(comment -> CommentResponse.mapTo(comment))
                .collect(Collectors.toList());
    }
}
