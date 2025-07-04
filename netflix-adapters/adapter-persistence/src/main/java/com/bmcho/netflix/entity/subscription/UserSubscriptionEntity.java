package com.bmcho.netflix.entity.subscription;

import com.bmcho.netflix.entity.audit.MutableBaseEntity;
import com.bmcho.netflix.enums.SubscriptionType;
import com.bmcho.netflix.subscription.UserSubscription;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "user_subscriptons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSubscriptionEntity extends MutableBaseEntity {

    @Id
    @Column(name = "USER_SUBSCRIPTION_ID")
    private String userSubscriptionId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "SUBSCRIPTION_NAME")
    @Enumerated(value = EnumType.STRING)
    private SubscriptionType subscriptionName;

    @Column(name ="SUBSCRIPTION_START_AT")
    private LocalDateTime subscriptionStartAt;

    @Column(name ="SUBSCRIPTION_END_AT")
    private LocalDateTime subscriptionEndAt;

    @Column(name = "VALID_YN")
    private Boolean validYn;

    public UserSubscription toDomain() {
        return UserSubscription.builder()
            .userId(this.userId)
            .subscriptionType(this.subscriptionName)
            .startAt(this.subscriptionStartAt)
            .endAt(this.subscriptionEndAt)
            .validYn(this.validYn)
            .build();
    }

    public static UserSubscriptionEntity toEntity(UserSubscription userSubscription) {
        return new UserSubscriptionEntity(
            UUID.randomUUID().toString(),
            userSubscription.getUserId(),
            userSubscription.getSubscriptionType(),
            userSubscription.getStartAt(),
            userSubscription.getEndAt(),
            userSubscription.getValidYn()
        );
    }

}
