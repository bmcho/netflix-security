package com.bmcho.netflix.user.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record UserRegistrationCommand(
    String username,
    String encryptedPassword,
    String email,
    String phone
) {
}
