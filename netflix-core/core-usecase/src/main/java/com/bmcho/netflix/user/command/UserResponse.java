package com.bmcho.netflix.user.command;

import lombok.Builder;

@Builder
public record UserResponse(
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
