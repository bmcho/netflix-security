package com.bmcho.netflix.respository.subscription;

import com.bmcho.netfilx.subscription.FetchUserSubscriptionPort;
import com.bmcho.netfilx.subscription.InsertUserSubscriptionPort;
import com.bmcho.netfilx.subscription.UpdateUserSubscriptionPort;
import com.bmcho.netflix.entity.subscription.UserSubscriptionEntity;
import com.bmcho.netflix.subscription.UserSubscription;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSubscriptionRepository implements InsertUserSubscriptionPort, UpdateUserSubscriptionPort, FetchUserSubscriptionPort {

    private final UserSubscriptionJpaRepository userSubscriptionJpaRepository;

    @Override
    @Transactional
    public Optional<UserSubscription> findByUserId(String userId) {
        Optional<UserSubscriptionEntity> userSubscriptionEntity = userSubscriptionJpaRepository.findByUserId(userId);
        return userSubscriptionEntity.map(UserSubscriptionEntity::toDomain);
    }

    @Override
    @Transactional
    public void create(String userId) {
        userSubscriptionJpaRepository.save(
            UserSubscriptionEntity.toEntity(
                UserSubscription.newSubscription(userId)
            )
        );
    }

    @Override
    @Transactional
    public void update(UserSubscription userSubscription) {
        userSubscriptionJpaRepository.save(
            UserSubscriptionEntity.toEntity(userSubscription)
        );
    }
}
