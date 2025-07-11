package com.bmcho.netflix.config.OAuth2.adapter;

import com.bmcho.netflix.config.OAuth2.OAuth2UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KakaoOAuth2UserAttributesAdapter implements OAuth2UserAttributesAdapter {

    @Override
    public boolean support(String registrationId) {
        return StringUtils.equalsIgnoreCase("kakao", registrationId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public OAuth2UserInfo extract(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        // Kakao는 id + kakao_account 안에 profile
        Map<String, Object> extractedAttributes = new HashMap<>();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        extractedAttributes.put("id", attributes.get("id"));

        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        extractedAttributes.put("nickname", properties.get("nickname"));
        extractedAttributes.put("provider", userRequest.getClientRegistration().getRegistrationId());
        String nameAttributeKey = userRequest
            .getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

        return new OAuth2UserInfo(extractedAttributes, nameAttributeKey);
    }
}
