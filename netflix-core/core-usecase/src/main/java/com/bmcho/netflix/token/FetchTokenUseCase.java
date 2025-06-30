package com.bmcho.netflix.token;

import com.bmcho.netflix.token.response.TokenResponse;
import com.bmcho.netflix.user.command.UserResponse;

import java.util.Optional;

public interface FetchTokenUseCase {
    Boolean validateToken(String accessToken);

    String getTokenFromKakao(String code);

    UserResponse fineUserByAccessToken(String accessToken);
}
