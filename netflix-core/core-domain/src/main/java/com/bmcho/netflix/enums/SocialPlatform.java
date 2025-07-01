package com.bmcho.netflix.enums;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@RequiredArgsConstructor
public enum SocialPlatform {
    KAKAO("kakao"),
    NAVER("naver"),
    GOGLE("gogle");

    public static boolean validationByName(String name) {
        return Arrays.stream(values())
            .anyMatch(value -> StringUtils.equalsIgnoreCase(value.provider, name));
    }

    private final String provider;
}
