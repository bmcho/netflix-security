package com.bmcho.netflix.config.OAuth2.adapter;

import com.bmcho.netflix.config.OAuth2.OAuth2UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GoggleOAuth2UserAttributesAdapter implements OAuth2UserAttributesAdapter {

    @Override
    public boolean support(String registrationId) {
        return StringUtils.equalsIgnoreCase("gogle", registrationId);
    }

    @Override
    public OAuth2UserInfo extract(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String nameAttributeKey = userRequest
            .getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();
        return new OAuth2UserInfo(oAuth2User.getAttributes(), nameAttributeKey);
    }

}
