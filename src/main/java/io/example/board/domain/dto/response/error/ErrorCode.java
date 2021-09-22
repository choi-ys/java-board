package io.example.board.domain.dto.response.error;

/**
 * @author : choi-ys
 * @date : 2021/09/21 7:25 오전
 */
public enum ErrorCode {
    BAD_CREDENTIALS("사용자 정보를 찾을 수 없습니다."),
    UNAUTHORIZED("유효한 자격 증명이 아닙니다."),
    ACCESS_DENIED("요청에 필요한 권한이 부족합니다."),
    HTTP_MESSAGE_NOT_READABLE("요청값을 확인 할 수 없습니다. 요청값을 확인해주세요."),
    METHOD_ARGUMENT_NOT_VALID("잘못된 요청입니다. 요청값을 확인해주세요."),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED("허용하지 않는 Http Method 요청입니다."),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED("지원하지 않는 Media Type 입니다.");

    public String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
