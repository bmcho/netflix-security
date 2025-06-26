package com.bmcho.netflix.advice;

import com.bmcho.netflix.controller.NetflixApiResponse;
import com.bmcho.netflix.exception.ErrorCode;
import com.bmcho.netflix.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(UserException.class)
    protected NetflixApiResponse<?> handleUserException(UserException userException) {
        log.error("error={}", userException.getMessage(), userException);
        ErrorCode errorCode = userException.getCode();
        return NetflixApiResponse.fail(errorCode.getCode(), errorCode.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    protected NetflixApiResponse<?> handleRuntimeException(RuntimeException runtimeException) {
        log.error("error={}", runtimeException.getMessage(), runtimeException);

        return NetflixApiResponse.fail(ErrorCode.DEFAULT_ERROR.getCode(), runtimeException.getMessage());
    }
}
