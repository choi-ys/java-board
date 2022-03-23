package io.example.board.repository.rdb.post;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import io.example.board.domain.dto.request.SearchPostRequest;
import io.example.board.domain.dto.response.SearchPostResponse;
import io.example.board.domain.dto.response.SearchPostWithCommentsResponse;
import io.example.board.domain.rdb.post.Post;
import io.example.board.domain.rdb.post.QComment;
import io.example.board.domain.rdb.post.QPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : choi-ys
 * @date : 2021/12/13 1:48 오후
 */
public class PostQueryRepoImpl extends QuerydslRepositorySupport implements PostQueryRepo {
    QPost post = QPost.post;
    QComment comment = QComment.comment;

    public PostQueryRepoImpl() {
        super(Post.class);
    }

    @Override
    public Page<SearchPostResponse> findPostPageBySearchParams(SearchPostRequest searchPostRequest) {

        /**
         * Entity에서 질의 대상 항목만 선정하는 Custom Projection 구성 방법
         * 1. 생성자
         * 2. Setter
         * 3. QueryProjection Annotation
         */
        JPQLQuery<SearchPostResponse> query = from(post)
                .select(Projections.constructor(SearchPostResponse.class,
                        post.id,
                        post.title,
                        post.content,
                        post.viewCount,
                        post.createdAt,
                        post.updatedAt,
                        post.member
                ))
                .where(
                        likePostTitle(searchPostRequest.getTitle()),
                        likePostContent(searchPostRequest.getContent()),
                        likePostWriterName(searchPostRequest.getWriterName()),
                        postCreatedAtGoe(searchPostRequest.getCreatedAt()),
                        postUpdatedAtLoe(searchPostRequest.getUpdatedAt())
                );

        return new PageImpl<>(Objects.requireNonNull(getQuerydsl())
                .applyPagination(searchPostRequest.getPageable(), query)
                .fetch(),
                searchPostRequest.getPageable(),
                query.fetchCount()
        );
    }

    @Override
    public Page<SearchPostWithCommentsResponse> findPostWithCommentsPageBySearchParams(SearchPostRequest searchPostRequest) {
        JPQLQuery<Post> query = from(post)
                .select(post)
                .innerJoin(post.member).fetchJoin()
                .leftJoin(post.comments, comment)
                .groupBy(post)
                .where(
                        likePostTitle(searchPostRequest.getTitle()),
                        likePostContent(searchPostRequest.getContent()),
                        likePostWriterName(searchPostRequest.getWriterName()),
                        postCreatedAtGoe(searchPostRequest.getCreatedAt()),
                        postUpdatedAtLoe(searchPostRequest.getUpdatedAt())
                );

        return new PageImpl<>(Objects.requireNonNull(getQuerydsl())
                .applyPagination(searchPostRequest.getPageable(), query)
                .fetch().stream()
                .map(post -> new SearchPostWithCommentsResponse(
                        post,
                        post.getMember(),
                        post.getComments())
                )
                .collect(Collectors.toList()),
                searchPostRequest.getPageable(),
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
