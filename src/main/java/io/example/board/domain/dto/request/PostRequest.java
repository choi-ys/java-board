package io.example.board.domain.dto.request;

import io.example.board.domain.rdb.member.Member;
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
public class PostRequest {

    private String title;

    private String content;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021-09-26 오후 4:49
    // * --------------------------------------------------------------
    public PostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // * --------------------------------------------------------------
    // * Header : 객체 값 매핑
    // * @author : choi-ys
    // * @date : 2021-09-26 오후 4:49
    // * --------------------------------------------------------------
    public Post toEntity(Member member) {
        return new Post(title, content, member);
    }
}
