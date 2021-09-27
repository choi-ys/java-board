package io.example.board.utils.generator.mock;

import io.example.board.domain.dto.request.PostCreateRequest;
import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Post;
import io.example.board.repository.rdb.post.PostRepo;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오전 1:20
 */
@TestComponent
@Import(MemberGenerator.class)
// TODO: 용석(2021-09-27) : assertions saved object (Assert.notNull(id, "Entity must be saved")
public class PostGenerator {

    private final MemberGenerator memberGenerator;
    private final PostRepo postRepo;

    public static final String title = "게시글 제목";
    public static final String content = "게시글 본문";

    public PostGenerator(MemberGenerator memberGenerator, PostRepo postRepo) {
        this.memberGenerator = memberGenerator;
        this.postRepo = postRepo;
    }

    public Post post() {
        return new Post(title, content, memberGenerator.savedMember());
    }

    public Post post(Member savedMember) {
        return new Post(title, content, savedMember);
    }

    public Post savedPost() {
        return postRepo.save(post());
    }

    public Post savedPost(Member savedMember) {
        return postRepo.save(post(savedMember));
    }

    public PostUpdateRequest postUpdateRequest(Post savedPost) {
        return new PostUpdateRequest(
                savedPost.getId(),
                "수정된 제목",
                "수정된 본문",
                true
        );
    }

    public static Post postMock() {
        return new Post(title, content, MemberGenerator.member());
    }

    public static PostCreateRequest postRequest() {
        return new PostCreateRequest(title, content);
    }

    public static PostUpdateRequest postUpdateRequestMock() {
        return new PostUpdateRequest(
                0L,
                "수정된 제목",
                "수정된 본문",
                true
        );
    }
}
