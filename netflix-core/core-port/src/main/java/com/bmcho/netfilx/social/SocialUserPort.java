package com.bmcho.netfilx.social;

import com.bmcho.netfilx.user.UserPortResponse;

public interface SocialUserPort {
    UserPortResponse findUserByAccessToken(String accessToken);
}
