package com.bmcho.netflix.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    DEFAULT_ERROR("NFX0000", "에러가 발생했습니다."),
    PASSWORD_ENCRYPTION_FAILED("NFX1000", "비밀번호 암호화 중 에러가 발생했습니다."),
    NETFLIX_UNSUPPORTED_SOCIAL_LOGIN("NFX1001", "지원하지 않는 소셜 로그인"),
    USER_ALREADY_EXIST("NFX2000", "사용자가 이미 존재합니다."),
    USER_DOES_NOT_EXIST("NFX2001", "사용자가 존재하지 않습니다."),
    AUTHENTICATION_FAILED("NFX2002", "인증에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요."),
    NO_MORE_MOVIE_DOWNLOAD("NFX2003", "더 이상 영화를 다운로드 할 수 없습니다."),
    ACCESS_DENIED("NFX2004", "해당 기능에 접근이 제한됩니다.")
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.code, this.message);
    }
}
