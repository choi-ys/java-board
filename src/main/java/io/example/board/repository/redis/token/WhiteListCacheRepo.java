package io.example.board.repository.redis.token;

import io.example.board.domain.redis.token.WhiteListCache;
import org.springframework.data.repository.CrudRepository;

/**
 * @author : choi-ys
 * @date : 2021/09/24 1:21 오전
 */
public interface WhiteListCacheRepo extends CrudRepository<WhiteListCache, String> {
}
