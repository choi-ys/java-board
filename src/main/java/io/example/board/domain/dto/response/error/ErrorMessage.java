package io.example.board.domain.dto.response.error;

/**
 * @author : choi-ys
 * @date : 2021/09/21 7:25 오전
 */
public enum ErrorMessage {
    HTTP_MESSAGE_NOT_READABLE("요청값을 확인 할 수 없습니다. 요청값을 확인해주세요."),
    METHOD_ARGUMENT_NOT_VALID("잘못된 요청입니다. 요청값을 확인해주세요."),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED("허용하지 않는 Http Method 요청입니다."),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED("지원하지 않는 Media Type 입니다.");

    public String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
