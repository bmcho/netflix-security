package com.bmcho.netflix.advice;

import com.bmcho.netflix.controller.NetflixApiResponse;
import com.bmcho.netflix.exception.ErrorCode;
import com.bmcho.netflix.exception.NetflixException;
import com.bmcho.netflix.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import static com.bmcho.netflix.exception.ErrorCode.*;

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

        String errorMessage = runtimeException.getMessage();
        if (errorMessage == null) {
            errorMessage = DEFAULT_ERROR.getMessage();
        }

        return NetflixApiResponse.fail(DEFAULT_ERROR.getCode(), errorMessage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected NetflixApiResponse<?> handleBadCredentialsException(BadCredentialsException e) {
        log.error("Authentication failed: {}", e.getMessage(), e);
        return NetflixApiResponse.fail(AUTHENTICATION_FAILED.getCode(), AUTHENTICATION_FAILED.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    protected NetflixApiResponse<?> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error("AuthorizationDeniedException : {}", e.getMessage(), e);
        return NetflixApiResponse.fail(ACCESS_DENIED.getCode(), ACCESS_DENIED.getMessage());
    }

    @ExceptionHandler(NetflixException.class)
    protected NetflixApiResponse<?> handleBadCredentialsException(NetflixException netflixException) {
        log.error("netflixException: {}", netflixException.getMessage(), netflixException);
        ErrorCode errorCode = netflixException.getCode();
        return NetflixApiResponse.fail(errorCode.getCode(), errorCode.getMessage());
    }
}
