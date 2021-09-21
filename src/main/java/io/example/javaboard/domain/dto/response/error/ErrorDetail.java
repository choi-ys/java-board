package io.example.javaboard.domain.dto.response.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : choi-ys
 * @date : 2021/09/21 2:58 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDetail {
    private String object;
    private String field;
    private String code;
    private String rejectMessage;
    private Object rejectedValue;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/21 6:22 오후
    // * --------------------------------------------------------------
    private ErrorDetail(String object, String field, String code, String rejectMessage, Object rejectedValue) {
        this.object = object;
        this.field = field;
        this.code = code;
        this.rejectMessage = rejectMessage;
        this.rejectedValue = rejectedValue;
    }

    // * --------------------------------------------------------------
    // * Header : 객체 값 매핑
    // * @author : choi-ys
    // * @date : 2021/09/21 6:23 오후
    // * --------------------------------------------------------------
    public static List<ErrorDetail> mapTo(List<FieldError> fieldErrors) {
        return fieldErrors.stream().map(it ->
                new ErrorDetail(
                        it.getObjectName(),
                        it.getField(),
                        it.getCode(),
                        it.getDefaultMessage(),
                        it.getRejectedValue()
                )
        ).collect(Collectors.toList());
    }
}
