package io.example.board.advicd.exception;

import io.example.board.domain.dto.response.error.ErrorMessage;
import io.example.board.domain.dto.response.error.ErrorResource;
import io.example.board.domain.dto.response.error.MethodArgumentNotValidErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : choi-ys
 * @date : 2021/09/21 6:16 오전
 */
@RestControllerAdvice
public class ExceptionAdvice {

    // [400] @Valid를 이용한 유효성 검사 시, @RequestBody의 값이 없는 경우 JSR 380 Annotations이 적용된 필드의 Binding Exception
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResource(
                        ErrorMessage.HTTP_MESSAGE_NOT_READABLE,
                        request)
                );
    }

    // [400] @Valid를 이용한 유효성 검사 시, @RequestBody의 값이 잘못된 경우 JSR 380 Annotations이 적용된 필드의 Binding Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new MethodArgumentNotValidErrorResource(
                        ErrorMessage.METHOD_ARGUMENT_NOT_VALID,
                        request,
                        exception.getFieldErrors())
                );
    }

    // [405] 허용하지 않는 Http Method 요청인 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResource(
                        ErrorMessage.HTTP_REQUEST_METHOD_NOT_SUPPORTED,
                        request)
                );
    }

    // [415] 요청 Media Type이 잘못된 경우
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorResource(
                ErrorMessage.HTTP_MEDIA_TYPE_NOT_SUPPORTED,
                request)
        );
    }
}
