package io.example.board.repository.rdb.post;

import io.example.board.domain.dto.request.SearchPostRequest;
import io.example.board.domain.dto.response.SearchPostResponse;
import io.example.board.domain.dto.response.SearchPostWithCommentsResponse;
import org.springframework.data.domain.Page;

/**
 * @author : choi-ys
 * @date : 2021/12/13 1:48 오후
 */
public interface PostQueryRepo {
    Page<SearchPostResponse> findPostPageBySearchParams(SearchPostRequest searchPostRequest);

    Page<SearchPostWithCommentsResponse> findPostWithCommentsPageBySearchParams(SearchPostRequest searchPostRequest);
}
