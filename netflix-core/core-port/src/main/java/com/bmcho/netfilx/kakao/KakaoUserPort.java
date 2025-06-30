package com.bmcho.netfilx.kakao;

import com.bmcho.netfilx.user.UserPortResponse;

public interface KakaoUserPort {
    UserPortResponse findUserFromKakao(String accessToken);
}
