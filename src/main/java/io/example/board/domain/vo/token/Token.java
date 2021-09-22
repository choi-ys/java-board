package io.example.board.domain.vo.token;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : choi-ys
 * @date : 2021/09/22 1:45 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Token {

    private String accessToken;

    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss", timezone = "Asia/Seoul")
    private Date accessExpired;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss", timezone = "Asia/Seoul")
    private Date refreshExpired;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/22 1:48 오전
    // * --------------------------------------------------------------
    public Token(String accessToken, String refreshToken, Date accessExpired, Date refreshExpired) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessExpired = accessExpired;
        this.refreshExpired = refreshExpired;
    }
}
