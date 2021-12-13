package io.example.board.repository.rdb.post;

import io.example.board.domain.dto.request.PostSearchRequest;
import io.example.board.domain.dto.response.PostSearchResponse;
import org.springframework.data.domain.Page;

/**
 * @author : choi-ys
 * @date : 2021/12/13 1:48 오후
 */
public interface PostQueryRepo {
    Page<PostSearchResponse> findPostPageBySearchParams(PostSearchRequest postSearchRequest);
}
