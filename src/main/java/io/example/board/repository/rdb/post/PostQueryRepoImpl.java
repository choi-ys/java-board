package io.example.board.repository.rdb.post;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import io.example.board.domain.dto.request.PostSearchRequest;
import io.example.board.domain.dto.response.PostSearchResponse;
import io.example.board.domain.rdb.post.Post;
import io.example.board.domain.rdb.post.QPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author : choi-ys
 * @date : 2021/12/13 1:48 오후
 */
public class PostQueryRepoImpl extends QuerydslRepositorySupport implements PostQueryRepo {
    QPost post = QPost.post;

    public PostQueryRepoImpl() {
        super(Post.class);
    }

    @Override
    public Page<PostSearchResponse> findPostPageBySearchParams(PostSearchRequest postSearchRequest) {

        /**
         * Entity에서 질의 대상 항목만 선정하는 Custom Projection 구성 방법
         * 1. 생성자
         * 2. Setter
         * 3. QueryProjection Annotation
         */
        JPQLQuery<PostSearchResponse> query = from(post)
                .select(Projections.constructor(PostSearchResponse.class,
                        post.id,
                        post.title,
                        post.content,
                        post.viewCount,
                        post.createdAt,
                        post.updatedAt,
                        post.member
                ))
                .where(
                        likePostTitle(postSearchRequest.getTitle()),
                        likePostContent(postSearchRequest.getContent()),
                        likePostWriterName(postSearchRequest.getWriterName()),
                        postCreatedAtGoe(postSearchRequest.getCreatedAt()),
                        postUpdatedAtLoe(postSearchRequest.getUpdatedAt())
                );

        return new PageImpl<>(getQuerydsl()
                .applyPagination(postSearchRequest.getPageable(), query)
                .fetch(),
                postSearchRequest.getPageable(),
                query.fetchCount()
        );
    }

    private Predicate likePostTitle(String title) {
        return StringUtils.hasText(title) ? post.title.containsIgnoreCase(title) : null;
    }

    private Predicate likePostContent(String content) {
        return StringUtils.hasText(content) ? post.content.containsIgnoreCase(content) : null;
    }

    private Predicate likePostWriterName(String writerName) {
        return StringUtils.hasText(writerName) ? post.member.name.eq(writerName) : null;
    }

    private Predicate postCreatedAtGoe(LocalDateTime createdAt) {
        return createdAt != null ? post.createdAt.goe(createdAt) : null;
    }

    private Predicate postUpdatedAtLoe(LocalDateTime updatedAt) {
        return updatedAt != null ? post.updatedAt.loe(updatedAt) : null;
    }
}
