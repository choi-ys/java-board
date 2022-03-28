package io.example.board.domain.dto.response.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/09/21 4:30 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodArgumentNotValidErrorResponse extends ErrorResponse {
    private List<ErrorDetail> errorDetails;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/21 6:24 오후
    // * --------------------------------------------------------------
    public MethodArgumentNotValidErrorResponse(ErrorCode errorCode, HttpServletRequest httpServletRequest, List<FieldError> fieldErrors) {
        super(errorCode, httpServletRequest);
        this.errorDetails = ErrorDetail.mapTo(fieldErrors);
    }
}
