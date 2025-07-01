package com.bmcho.netflix.social;

import com.bmcho.netfilx.social.SocialPlatformPort;
import com.bmcho.netfilx.user.UserPortResponse;
import com.bmcho.netflix.enums.SocialPlatform;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoHttpClient implements SocialPlatformPort {

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserinfoApiUri;

    @Override
    public boolean supports(String provider) {
        return StringUtils.equalsIgnoreCase(SocialPlatform.KAKAO.name(), provider);
    }

    @Override
    public String getAccessTokenByCode(String code) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("client_secret", kakaoClientSecret);  // 선택사항
        params.add("code", code);  // 카카오로부터 받은 인증 코드

        // Http - Headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            kakaoTokenUri,
            HttpMethod.POST,
            request,
            Map.class);

        return (String) response.getBody().get("access_token");
    }

    @Override
    public UserPortResponse findUserByAccessToken(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);  // 액세스 토큰을 Authorization Header에 추가

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
            kakaoUserinfoApiUri,
            HttpMethod.GET,
            httpEntity,
            Map.class
        );

        Long providerId = (Long) response.getBody().get("id");
        Map properties = (Map) response.getBody().get("kakao_account");
        String username = (String) properties.get("name");

        return UserPortResponse.builder()
            .username(username)
            .providerId(providerId.toString())
            .provider("kakao")
            .build();
    }
}
