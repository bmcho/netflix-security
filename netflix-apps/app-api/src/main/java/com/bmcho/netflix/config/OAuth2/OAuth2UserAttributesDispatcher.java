package com.bmcho.netflix.config.OAuth2;

import com.bmcho.netflix.config.OAuth2.adapter.OAuth2UserAttributesAdapter;
import com.bmcho.netflix.exception.NetflixException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2UserAttributesDispatcher {

    private final List<OAuth2UserAttributesAdapter> adapters;

    public OAuth2UserInfo doDispatcher(String registrationId, OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        return adapters.stream()
            .filter(adapter -> adapter.support(registrationId))
            .findFirst()
            .orElseThrow(NetflixException.NetflixUnsupportedSocialLoginException::new)
            .extract(oAuth2UserRequest, oAuth2User);
    }

}
