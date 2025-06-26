package com.bmcho.netfilx.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record UserPortResponse(
    String userId,
    String userName,
    String password,
    String email,
    String phone,
    String provider,
    String providerId,
    String role
) {
}
