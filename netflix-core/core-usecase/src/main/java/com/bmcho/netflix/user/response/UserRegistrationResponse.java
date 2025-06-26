package com.bmcho.netflix.user.response;

import lombok.Getter;

@Getter
public record UserRegistrationResponse(
    String username,
    String email,
    String phone
) {
}
