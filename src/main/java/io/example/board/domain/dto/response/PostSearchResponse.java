package io.example.board.domain.dto.response;

import io.example.board.domain.rdb.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author : choi-ys
 * @date : 2021/12/13 4:54 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSearchResponse {
    private Long id;
    private String title;
    private String content;
    private long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MemberSimpleResponse writer;

    public PostSearchResponse(
            Long id,
            String title,
            String content,
            long viewCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Member member
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.writer = MemberSimpleResponse.mapTo(member);
    }
}
