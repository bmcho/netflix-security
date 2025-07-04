package com.bmcho.netfilx.user;

import lombok.Builder;

@Builder
public record CreateUserHistory(
    String userId,
    String userRole,
    String clientIp,
    String reqMethod,
    String reqUrl,
    String reqHeader,
    String reqPayload
) {
}
