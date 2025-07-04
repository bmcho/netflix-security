package com.bmcho.netflix.enums;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum SocialPlatformType {
    KAKAO("kakao"),
    NAVER("naver"),
    GOGLE("gogle");

    public static boolean validationByName(String name) {
        return Arrays.stream(values())
            .anyMatch(value -> StringUtils.equalsIgnoreCase(value.provider, name));
    }

    SocialPlatformType(String provider) {
        this.provider = provider;
    }
    private final String provider;
}
