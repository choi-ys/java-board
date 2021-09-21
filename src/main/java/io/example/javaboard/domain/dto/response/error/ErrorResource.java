package io.example.javaboard.domain.dto.response.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String code;
    private String message;

    private String method;
    private String path;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/21 6:23 오후
    // * --------------------------------------------------------------
    public ErrorResource(ErrorMessage errorMessage, HttpServletRequest httpServletRequest) {
        this.code = errorMessage.name();
        this.message = errorMessage.message;
        this.method = httpServletRequest.getMethod();
        this.path = httpServletRequest.getPathInfo();
        timestamp = LocalDateTime.now();
    }
}
