package com.bmcho.netflix.user;

import com.bmcho.netflix.user.command.UserResponse;

public interface FetchUserUseCase {
    UserResponse findUserByUserId(String userId);

    UserResponse findByEmail(String email);

    UserResponse findByProviderId(String providerId);

    UserResponse findUserAccessToken(String provider, String accessToken);
}
