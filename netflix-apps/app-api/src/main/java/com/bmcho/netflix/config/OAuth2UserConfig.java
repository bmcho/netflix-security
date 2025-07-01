package com.bmcho.netflix.config;

import com.bmcho.netflix.enums.SocialPlatform;
import com.bmcho.netflix.exception.NetflixException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2UserConfig implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "kakao", "naver", "google", etc.

        if (!SocialPlatform.validationByName(registrationId)) {
            throw new NetflixException.NetflixUnsupportedSocialLoginException();
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> extractedAttributes = new HashMap<>();
        extractedAttributes.put("provider", registrationId);
        String nameAttributeKey;

        switch (registrationId.toLowerCase()) {
            case "naver":
                // 네이버는 response 안에 있음
                extractedAttributes.putAll((Map<String, Object>) attributes.get("response"));
                nameAttributeKey = "id";
                break;

            case "kakao":
                // Kakao는 id + kakao_account 안에 profile
                extractedAttributes = new HashMap<>();
                extractedAttributes.put("id", attributes.get("id"));

                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

                extractedAttributes.put("email", kakaoAccount.get("email"));
                extractedAttributes.put("nickname", profile.get("nickname"));
                extractedAttributes.put("provider", registrationId);
                nameAttributeKey = "id";
                break;

            default:
                // 구글, 깃허브 등은 평평한 구조
                extractedAttributes = attributes;
                nameAttributeKey = userRequest
                    .getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName(); // 보통 "sub"
                break;
        }

//        Collection<? extends GrantedAuthority> authorities =
//            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return new DefaultOAuth2User(oAuth2User.getAuthorities(), extractedAttributes, nameAttributeKey);
    }
}