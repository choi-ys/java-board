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

    /**
     * [500] 서버 에러 : exception global exception handling
     *
     * @param exception Exception, Exception을 상속받는 하위 Exception, Exception이 StackTrace에 포함된 Exception
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity unexpectedException(Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        return ResponseEntity.internalServerError()
                .body(new ErrorResource(ErrorCode.SERVER_ERROR, request));
    }

    /**
     * [500] 서버 에러 : exception global RuntimeException handling
     *
     * @param runtimeException only RuntimeException
     *                         - @ExceptionHandler(RuntimeException.class)를 이용, 해당 Handler가 RuntimeException만 handling 할 수 있게 처리
     *                         - RuntimeExcpeiton을 상속하거나, RuntimeException이 StackTrace에 포함된 경우에도 handling 하지 않음
     * @param request
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity unexpectedRuntimeException(RuntimeException runtimeException, HttpServletRequest request) {
        runtimeException.printStackTrace();
        return ResponseEntity.internalServerError()
                .body(new ErrorResource(ErrorCode.SERVER_ERROR, request));
    }
}
