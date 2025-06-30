package com.bmcho.netfilx.token;

public record TokenPortResponse(
    String accessToken,
    String refreshToken
) {
}
