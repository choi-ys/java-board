package io.example.board.repository.rdb.post;

import io.example.board.domain.rdb.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오전 5:07
 */
public interface PostRepo extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = "member")
    Optional<Post> findByIdAndDisplay(Long aLong, boolean display);
}
