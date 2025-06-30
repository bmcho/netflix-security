package com.bmcho.netflix.respository.token;

import com.bmcho.netfilx.token.InsertTokenPort;
import com.bmcho.netfilx.token.SearchTokenPort;
import com.bmcho.netfilx.token.TokenPortResponse;
import com.bmcho.netfilx.token.UpdateTokenPort;
import com.bmcho.netflix.entity.token.TokenEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenRepository implements InsertTokenPort, UpdateTokenPort, SearchTokenPort {

    private final TokenJpaRepository tokenJpaRepository;

    @Override
    @Transactional
    public TokenPortResponse create(String userId, String accessToken, String refreshToken) {
        TokenEntity tokenEntity = TokenEntity.toEntity(userId, accessToken, refreshToken);
        tokenJpaRepository.save(tokenEntity);
        return new TokenPortResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public TokenPortResponse findByUserId(String userId) {
        return tokenJpaRepository.findByUserId(userId)
            .map(it -> new TokenPortResponse(it.getAccessToken(), it.getRefreshToken()))
            .orElse(null);
    }

    @Override
    @Transactional
    public void updateToken(String userId, String accessToken, String refreshToken) {
        Optional<TokenEntity> byUserId = tokenJpaRepository.findByUserId(userId);
        if (byUserId.isEmpty()) {
            throw new RuntimeException();
        }
        TokenEntity tokenEntity = byUserId.get();
        tokenEntity.updateToken(accessToken, refreshToken);
        tokenJpaRepository.save(tokenEntity);
    }
}
