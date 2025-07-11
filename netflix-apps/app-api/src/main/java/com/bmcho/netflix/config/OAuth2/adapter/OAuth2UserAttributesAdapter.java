package com.bmcho.netflix.config.OAuth2.adapter;

import com.bmcho.netflix.config.OAuth2.OAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserAttributesAdapter {
    boolean support(String registrationId);

    OAuth2UserInfo extract(OAuth2UserRequest userRequest, OAuth2User oAuth2User);
}
