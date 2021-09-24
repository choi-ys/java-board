package io.example.board.domain.redis.token;

import io.example.board.domain.vo.token.Token;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author : choi-ys
 * @date : 2021/09/24 1:06 오전
 */
@RedisHash(
        value = "WHITE_LIST_", // key prefix = @RedisHash value + Redis Entity @Id value
        timeToLive = 600L // 600L = 600s = 10Min
)
@Getter
public class WhiteListCache {
    @Id
    private String id;
    private Token token;

    public WhiteListCache(String id, Token token) {
        this.id = id;
        this.token = token;
    }
}
