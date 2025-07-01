package com.bmcho.netfilx.social;

public interface SocialTokenPort {
    String getAccessTokenByCode(String code);
}
