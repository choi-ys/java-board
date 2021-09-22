package io.example.board.domain.dto.response.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author : choi-ys
 * @date : 2021/09/21 6:17 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResource {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
    private String message;

    private String method;
    private String path;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성 : 기본 오류 응답 형식
    // * @author : choi-ys
    // * @date : 2021/09/21 6:23 오후
    // * --------------------------------------------------------------
    public ErrorResource(ErrorCode errorCode, HttpServletRequest httpServletRequest) {
        this.code = errorCode.name();
        this.message = errorCode.message;
        this.method = httpServletRequest.getMethod();
        this.path = httpServletRequest.getPathInfo();
        timestamp = LocalDateTime.now();
    }

    // * --------------------------------------------------------------
    // * Header : 도메인 생성 : 사용자 정의 오류 메세지를 포함한 오류 응답 형식
    // * @author : choi-ys
    // * @date : 2021/09/23 3:02 오전
    // * --------------------------------------------------------------
    public ErrorResource(ErrorCode errorCode, String message, HttpServletRequest httpServletRequest) {
        this.code = errorCode.name();
        this.message = StringUtils.hasText(message) ? message : errorCode.message;
        this.method = httpServletRequest.getMethod();
        this.path = httpServletRequest.getPathInfo();
        timestamp = LocalDateTime.now();
    }
}
