package com.bmcho.netfilx.user;

import java.util.Optional;

public interface FetchUserPort {
    Optional<UserPortResponse> findByEmail(String email);

    Optional<UserPortResponse> findByProviderId(String providerId);

    Optional<UserPortResponse> findByUserId(String userId);
}
