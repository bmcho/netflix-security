package com.bmcho.netflix.token;

import com.bmcho.netflix.user.command.UserResponse;

public interface FetchTokenUseCase {
    Boolean validateToken(String accessToken);

    String getTokenByCode(String provider, String code);

    UserResponse findUserByAccessToken(String accessToken);
}
