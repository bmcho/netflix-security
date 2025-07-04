package com.bmcho.netfilx.subscription;

import com.bmcho.netflix.subscription.UserSubscription;

import java.util.Optional;

public interface FetchUserSubscriptionPort {
    Optional<UserSubscription> findByUserId(String userId);
}
