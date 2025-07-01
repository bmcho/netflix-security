package com.bmcho.netflix.dispatcher;

import com.bmcho.netfilx.social.SocialPlatformPort;
import com.bmcho.netflix.exception.NetflixException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SocialPlatformDispatcher {

    private final List<SocialPlatformPort> providers;

    public SocialPlatformPort getProvide(String provider) {
        return providers.stream()
            .filter(port -> port.supports(provider))
            .findFirst()
            .orElseThrow(NetflixException.NetflixUnsupportedSocialLoginException::new);
    }
}
