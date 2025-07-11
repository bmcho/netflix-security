package com.bmcho.netflix.config.OAuth2;

import java.util.Map;

public record OAuth2UserInfo(
    Map<String, Object> attributes,
    String nameAttributeKey
) {
}
