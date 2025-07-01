package com.bmcho.netfilx.social;

public interface SocialPlatformPort extends SocialUserPort, SocialTokenPort {
    boolean supports(String provider);
}
