package com.bmcho.netflix.exception;

import lombok.Getter;

@Getter
public class NetflixException extends RuntimeException{

    private final ErrorCode code;

    public NetflixException(ErrorCode code) {
        this.code = code;
    }

    public static class NetflixUnsupportedSocialLoginException extends NetflixException {
        public NetflixUnsupportedSocialLoginException() {
            super(ErrorCode.NETFLIX_UNSUPPORTED_SOCIAL_LOGIN);
        }
    }
}
