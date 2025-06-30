package com.bmcho.netflix.token;

import com.bmcho.netflix.token.response.TokenResponse;

public interface CreateTokenUseCase {
    TokenResponse createNewToken(String userId);
}
