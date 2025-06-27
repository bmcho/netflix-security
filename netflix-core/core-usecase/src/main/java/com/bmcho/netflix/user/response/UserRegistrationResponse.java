package com.bmcho.netflix.user.response;

public record UserRegistrationResponse(
    String username,
    String email,
    String phone
) {
}
