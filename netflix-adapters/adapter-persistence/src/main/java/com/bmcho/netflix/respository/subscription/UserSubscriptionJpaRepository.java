package com.bmcho.netflix.respository.subscription;

import com.bmcho.netflix.entity.subscription.UserSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSubscriptionJpaRepository extends JpaRepository<UserSubscriptionEntity, String> {
    Optional<UserSubscriptionEntity> findByUserId(String userId);
}
