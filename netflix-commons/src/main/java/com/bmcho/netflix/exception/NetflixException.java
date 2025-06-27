package com.bmcho.netflix.exception;

import lombok.Getter;

@Getter
public class NetflixException extends RuntimeException{

    private final ErrorCode code;

    public NetflixException(ErrorCode code) {
        this.code = code;
    }
}
