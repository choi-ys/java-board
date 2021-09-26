package io.example.board.advice.exception;

import io.example.board.domain.dto.response.error.ErrorCode;

/**
 * @author : choi-ys
 * @date : 2021-09-26 오후 11:57
 */
public class ResourceNotFoundException extends IllegalArgumentException {
    public ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND_EXCEPTION.message);
    }
}
