package io.example.board.domain.dto.response;

import io.example.board.domain.rdb.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : choi-ys
 * @date : 2021-09-26 오후 4:42
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private long viewCount;
    private boolean display;
    private MemberSimpleResponse writer;

    public PostResponse(Long id, String title, String content, long viewCount, boolean display, MemberSimpleResponse writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.display = display;
        this.writer = writer;
    }

    public static PostResponse mapTo(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getViewCount(),
                post.isDisplay(),
                MemberSimpleResponse.mapTo(post.getMember())
        );
    }
}
