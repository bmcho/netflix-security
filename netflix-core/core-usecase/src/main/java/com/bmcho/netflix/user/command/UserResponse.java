package com.bmcho.netflix.user.command;

import lombok.Builder;

@Builder
public record UserResponse(
    String userId,
    String username,
    String password,
    String email,
    String phone,
    String provider,
    String providerId,
    String role
) {
}
