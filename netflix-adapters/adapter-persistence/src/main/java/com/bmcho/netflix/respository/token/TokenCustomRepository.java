package com.bmcho.netflix.respository.token;

import com.bmcho.netflix.entity.token.TokenEntity;

import java.util.Optional;

public interface TokenCustomRepository {
    Optional<TokenEntity> findByUserId(String userId);
}
