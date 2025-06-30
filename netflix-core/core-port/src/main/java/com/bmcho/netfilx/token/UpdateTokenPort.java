package com.bmcho.netfilx.token;

public interface UpdateTokenPort {
    void updateToken(String userId, String accessToken, String refreshToken);
}
