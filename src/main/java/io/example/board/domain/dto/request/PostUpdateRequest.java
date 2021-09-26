package io.example.board.domain.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오후 11:10
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostUpdateRequest {

    private Long id;

    private String title;

    private String content;

    private boolean display;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021-09-26 오전 12:00
    // * --------------------------------------------------------------
    public PostUpdateRequest(Long id, String title, String content, boolean display) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.display = display;
    }
}
