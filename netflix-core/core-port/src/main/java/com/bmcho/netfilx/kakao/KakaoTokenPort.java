package com.bmcho.netfilx.kakao;

public interface KakaoTokenPort {
    String getAccessTokenByCode(String code);
}
