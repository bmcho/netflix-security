package com.bmcho.netflix.user.command;

import lombok.Builder;

@Builder
public record UserHistoryCommand(
    String userId,
    String userRole,
    String clientIp,
    String reqMethod,
    String reqUrl,
    String reqHeader,
    String reqPayload
) {
}
