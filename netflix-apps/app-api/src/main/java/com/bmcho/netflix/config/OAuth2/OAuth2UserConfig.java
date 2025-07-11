package com.bmcho.netflix.config.OAuth2;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuth2UserConfig implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final OAuth2UserAttributesDispatcher oAuth2UserAttributesDispatcher;

    public OAuth2UserConfig(OAuth2UserAttributesDispatcher oAuth2UserAttributesDispatcher) {
        this.oAuth2UserAttributesDispatcher = oAuth2UserAttributesDispatcher;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "kakao", "naver", "google"

        OAuth2UserInfo oAuth2UserInfo = oAuth2UserAttributesDispatcher.doDispatcher(
            registrationId, userRequest, oAuth2User
        );

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), oAuth2UserInfo.attributes(), oAuth2UserInfo.nameAttributeKey());
    }
}