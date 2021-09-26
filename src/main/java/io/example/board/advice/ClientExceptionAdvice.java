package io.example.board.advice;

import io.example.board.advice.exception.ResourceNotFoundException;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.dto.response.error.ErrorResource;
import io.example.board.domain.dto.response.error.MethodArgumentNotValidErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
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
public class ClientExceptionAdvice {

    // [400] @Valid를 이용한 유효성 검사 시, @RequestBody의 값이 없는 경우 JSR 380 Annotations이 적용된 필드의 Binding Exception
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResource(ErrorCode.HTTP_MESSAGE_NOT_READABLE, request));
    }

    // [400] @Valid를 이용한 유효성 검사 시, @RequestBody의 값이 잘못된 경우 JSR 380 Annotations이 적용된 필드의 Binding Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new MethodArgumentNotValidErrorResource(
                        ErrorCode.METHOD_ARGUMENT_NOT_VALID,
                        request,
                        exception.getFieldErrors())
                );
    }

    // [401] 유효한 자격 증명이 아닌 접근인 경우, Unauthorized Exception
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity authenticationException(Exception exception, HttpServletRequest request) {
        ErrorResource errorResource = exception instanceof BadCredentialsException ?
                new ErrorResource(ErrorCode.BAD_CREDENTIALS, exception.getMessage(), request) :
                new ErrorResource(ErrorCode.UNAUTHORIZED, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResource);
    }

    // [403] 리소스 접근에 필요한 권한이 존재 하지 않는 경우, Unauthorized Exception
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity accessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResource(ErrorCode.ACCESS_DENIED, exception.getMessage(), request));
    }

    // [404] 요청에 해당하는 자원이 존재하지 않는 경우
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity resourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResource(ErrorCode.RESOURCE_NOT_FOUND, request));
    }

    // [405] 허용하지 않는 Http Method 요청인 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResource(ErrorCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED, request));
    }

    // [406] 요청 Accept Type이 잘못된 경우
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity httpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResource(ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE, request));
    }

    // [415] 요청 Media Type이 잘못된 경우
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorResource(ErrorCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED, request));
    }
}
