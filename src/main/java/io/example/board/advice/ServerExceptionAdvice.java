package io.example.board.advice;

import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.dto.response.error.ErrorResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : choi-ys
 * @date : 2021/09/21 6:16 오전
 */
@RestControllerAdvice
public class ServerExceptionAdvice {

    // [500] 서버 에러 : exception global handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity unexpectedException(Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        return ResponseEntity.internalServerError()
                .body(new ErrorResource(ErrorCode.SERVER_ERROR, request));
    }
}
