package io.example.board.repository.rdb.comment;

import io.example.board.domain.rdb.post.Comment;
import io.example.board.domain.rdb.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author : choi-ys
 * @date : 2022/03/21 10:33 오전
 */
public interface CommentRepo extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = "post")
    List<Comment> findByPost(Post post);
}
