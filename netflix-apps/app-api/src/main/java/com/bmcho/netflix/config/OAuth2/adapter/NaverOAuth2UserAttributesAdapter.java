package com.bmcho.netflix.config.OAuth2.adapter;

import com.bmcho.netflix.config.OAuth2.OAuth2UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NaverOAuth2UserAttributesAdapter implements OAuth2UserAttributesAdapter {

    @Override
    public boolean support(String registrationId) {
        return StringUtils.equalsIgnoreCase("naver", registrationId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public OAuth2UserInfo extract(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        Map<String, Object> extractedAttributes =
            new HashMap<>((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        extractedAttributes.put("provider", userRequest.getClientRegistration().getRegistrationId());
        String nameAttributeKey = "id";

        return new OAuth2UserInfo(extractedAttributes, nameAttributeKey);
    }
}
