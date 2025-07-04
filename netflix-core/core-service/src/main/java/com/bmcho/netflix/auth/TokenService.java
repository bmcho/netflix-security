 package com.bmcho.netflix.auth;

import com.bmcho.netfilx.social.SocialPlatformPort;
import com.bmcho.netfilx.social.SocialTokenPort;
import com.bmcho.netfilx.token.InsertTokenPort;
import com.bmcho.netfilx.token.SearchTokenPort;
import com.bmcho.netfilx.token.TokenPortResponse;
import com.bmcho.netfilx.token.UpdateTokenPort;
import com.bmcho.netflix.dispatcher.SocialPlatformDispatcher;
import com.bmcho.netflix.exception.NetflixException;
import com.bmcho.netflix.token.CreateTokenUseCase;
import com.bmcho.netflix.token.FetchTokenUseCase;
import com.bmcho.netflix.token.UpdateTokenUseCase;
import com.bmcho.netflix.token.response.TokenResponse;
import com.bmcho.netflix.user.FetchUserUseCase;
import com.bmcho.netflix.user.command.UserResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService implements FetchTokenUseCase, UpdateTokenUseCase, CreateTokenUseCase {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expire.access-token}")
    private int accessTokenExpireHour;

    @Value("${jwt.expire.refresh-token}")
    private int refreshTokenExpireHour;



    private final SearchTokenPort searchTokenPort;
    private final InsertTokenPort insertTokenPort;
    private final UpdateTokenPort updateTokenPort;
    private final FetchUserUseCase fetchUserUseCase;
    private final SocialPlatformDispatcher socialPlatformDispatcher;

    @Override
    public TokenResponse createNewToken(String userId) {
        String accessToken = getToken(userId, Duration.ofHours(accessTokenExpireHour));
        String refreshToken = getToken(userId, Duration.ofHours(refreshTokenExpireHour));
        TokenPortResponse tokenPortResponse = insertTokenPort.create(userId, accessToken, refreshToken);

        return TokenResponse.builder()
            .accessToken(tokenPortResponse.accessToken())
            .refreshToken(tokenPortResponse.refreshToken())
            .build();
    }

    @Override
    public Boolean validateToken(String accessToken) {
        SecretKey key = getSigningKey();
        Jwts.parser()
            .verifyWith(key).build()
            .parseSignedClaims(accessToken);
        return true;
    }

    @Override
    public String getTokenByCode(String provider, String code) {
        SocialPlatformPort socialPlatformPort = socialPlatformDispatcher.getProvide(provider);
        return socialPlatformPort.getAccessTokenByCode(code);
    }

    @Override
    public UserResponse findUserByAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken);
        Object userId = claims.get("userId");

        if (ObjectUtils.isEmpty(userId)) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        return fetchUserUseCase.findByProviderId(userId.toString());
    }

    @Override
    public String updateInsertToken(String providerId) {
        TokenPortResponse tokenByUserId = searchTokenPort.findByUserId(providerId);
        String accessToken = getToken(providerId, Duration.ofHours(accessTokenExpireHour));
        String refreshToken = getToken(providerId, Duration.ofHours(refreshTokenExpireHour));

        // create
        if (tokenByUserId == null) {
            insertTokenPort.create(providerId, accessToken, refreshToken);
        }
        // update
        else {
            updateTokenPort.updateToken(providerId, accessToken, refreshToken);
        }

        return accessToken;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String getToken(String userId, Duration expireAt) {
        Date now = new Date();
        Instant instant = now.toInstant();

        return Jwts.builder()
            .claim("userId", userId)
            .issuedAt(now)
            .expiration(Date.from(instant.plus(expireAt)))
            .signWith(getSigningKey())
            .compact();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                .decryptWith(getSigningKey()) // SecretKey 타입
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
