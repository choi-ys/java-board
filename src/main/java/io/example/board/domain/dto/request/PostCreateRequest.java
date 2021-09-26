package io.example.board.domain.dto.request;

import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author : choi-ys
 * @date : 2021-09-26 오후 4:42
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    @Size(min = 20, max = 50, message = "비밀번호는 8~20자 이내로 입력하세요.")
    private String title;

    @NotBlank(message = "본문은 필수 입력 사항입니다.")
    private String content;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021-09-26 오후 4:49
    // * --------------------------------------------------------------
    public PostCreateRequest(String title, String content) {
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
