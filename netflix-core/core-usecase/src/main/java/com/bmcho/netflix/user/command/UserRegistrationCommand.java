package com.bmcho.netflix.user.command;

import lombok.Builder;

@Builder
public record UserRegistrationCommand(
    String username,
    String encryptedPassword,
    String email,
    String phone
) {
}
