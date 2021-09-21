package io.example.javaboard.domain.dto.response.error;

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
public class MethodArgumentNotValidErrorResource extends ErrorResource {
    private List<ErrorDetail> errorDetails;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/21 6:24 오후
    // * --------------------------------------------------------------
    public MethodArgumentNotValidErrorResource(ErrorMessage errorMessage, HttpServletRequest httpServletRequest, List<FieldError> fieldErrors) {
        super(errorMessage, httpServletRequest);
        this.errorDetails = ErrorDetail.mapTo(fieldErrors);
    }
}
