package io.example.board.domain.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : choi-ys
 * @date : 2021/09/23 5:01 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshTokenRequest {
    private String accessToken;
    private String refreshToken;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/23 5:29 오전
    // * --------------------------------------------------------------
    public RefreshTokenRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
