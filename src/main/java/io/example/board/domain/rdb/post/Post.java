package io.example.board.domain.rdb.post;

import io.example.board.domain.dto.request.PostUpdateDto;
import io.example.board.domain.rdb.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오전 3:57
 */
@Entity
@Table(name = "post_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    @Column(name = "display", nullable = false)
    private boolean display = true;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021-09-25 오전 3:59
    // * --------------------------------------------------------------
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    // * --------------------------------------------------------------
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021-09-25 오후 11:10
    // * --------------------------------------------------------------
    public void update(PostUpdateDto postUpdateDto) {
        this.title = postUpdateDto.getTitle();
        this.content = postUpdateDto.getContent();
        this.display = postUpdateDto.isDisplay();
    }
}
