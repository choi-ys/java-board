package io.example.board.domain.rdb.post;

import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.rdb.base.Auditor;
import io.example.board.domain.rdb.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오전 3:57
 */
@Entity
@Table(name = "post_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Auditor {

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

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

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
    // * Header : 양방향 연관관계 설정
    // * @author : choi-ys
    // * @date : 2022-03-21 오후 2:07
    // * --------------------------------------------------------------
    public void addComments(Comment comment) {
        this.comments.add(comment);
    }

    // * --------------------------------------------------------------
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021-09-25 오후 11:10
    // * --------------------------------------------------------------
    public void update(PostUpdateRequest postUpdateRequest) {
        this.title = postUpdateRequest.getTitle();
        this.content = postUpdateRequest.getContent();
        this.display = postUpdateRequest.isDisplay();
    }

    public void delete() {
        this.deleted = true;
    }
}
