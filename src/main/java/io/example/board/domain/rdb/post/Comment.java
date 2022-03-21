package io.example.board.domain.rdb.post;

import io.example.board.domain.rdb.base.Auditor;
import io.example.board.domain.rdb.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author : choi-ys
 * @date : 2022/03/21 10:23 오전
 */
@Entity
@Table(name = "comment_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    public String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment(String content, Post post, Member member) {
        this.content = content;
        this.post = post;
        this.member = member;
        this.post.addComments(this);
    }
}
