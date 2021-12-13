package io.example.board.repository.rdb.post;

import io.example.board.domain.rdb.post.Post;
import io.example.board.repository.rdb.post.PostQueryRepo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오전 5:07
 */
public interface PostRepo extends JpaRepository<Post, Long>, PostQueryRepo {

    @EntityGraph(attributePaths = "member")
    Optional<Post> findByIdAndDisplayTrue(Long id);

    @EntityGraph(attributePaths = "member")
    Optional<Post> findByIdAndMemberEmail(Long id, String email);
}
