package io.example.board.repository.rdb.post;

import io.example.board.domain.rdb.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오전 5:07
 */
public interface PostRepo extends JpaRepository<Post, Long> {
}
