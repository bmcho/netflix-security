package com.bmcho.netflix.enums;

import lombok.Getter;

@Getter
public enum SubscriptionType {

    FREE("free"), // 영화 조회만 가능
    BRONZE("bronze"), // 영화 조회 + 다운로드 5회 + 좋아요/싫어요
    SILVER("silver"), // 영화 조회 + 다운로드 10회 + 좋아요/싫어요
    GOLD("gold")  // 영화 조회 + 다운로드 무제한 + 좋아요/싫어요
    ;

    SubscriptionType(String type) {
        this.type = type;
    }

    private final String type;
}
