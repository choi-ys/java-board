package io.example.board.domain.dto.response.error;

/**
 * @author : choi-ys
 * @date : 2021/09/21 7:25 오전
 */
public enum ErrorCode {
    // 400
    HTTP_MESSAGE_NOT_READABLE("요청값을 확인 할 수 없습니다. 요청값을 확인해주세요."),
    METHOD_ARGUMENT_TYPE_MISMATCH("요청값의 자료형이 잘못되었습니다. 요청값을 확인해주세요."),
    METHOD_ARGUMENT_NOT_VALID("잘못된 요청입니다. 요청값을 확인해주세요."),

    // 401
    BAD_CREDENTIALS("잘못된 자격 증명입니다."),
    AUTHENTICATION_CREDENTIALS_NOT_FOUND("자격 증명 정보를 찾을 수 없습니다."),

    // 403
    UNAUTHORIZED("유효한 자격 증명이 아닙니다."),
    ACCESS_DENIED("요청에 필요한 권한이 부족합니다."),

    // 404
    RESOURCE_NOT_FOUND("요청에 해당하는 자원을 찾을 수 없습니다."),

    // 405
    HTTP_REQUEST_METHOD_NOT_SUPPORTED("허용하지 않는 Http Method 요청입니다."),

    // 406
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE("지원하지 않는 Accept Type 입니다."),

    // 415
    HTTP_MEDIA_TYPE_NOT_SUPPORTED("지원하지 않는 Media Type 입니다."),

    // 500
    SERVER_ERROR("알 수 없는 오류가 발생하였습니다.");

    public String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
