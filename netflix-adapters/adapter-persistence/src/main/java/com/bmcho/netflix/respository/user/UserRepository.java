package com.bmcho.netflix.respository.user;

import com.bmcho.netfilx.user.*;
import com.bmcho.netflix.entity.user.SocialUserEntity;
import com.bmcho.netflix.entity.user.UserEntity;
import com.bmcho.netflix.entity.user.UserHistoryEntity;
import com.bmcho.netflix.enums.SubscriptionType;
import com.bmcho.netflix.respository.subscription.UserSubscriptionRepository;
import com.bmcho.netflix.subscription.UserSubscription;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository implements InsertUserPort, FetchUserPort, UserHistoryPort {

    private final UserJpaRepository userJpaRepository;
    private final SocialUserJpaRepository socialUserJpaRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserHistoryJpaRepository userHistoryJpaRepository;

    @Override
    public Optional<UserPortResponse> findByEmail(String email) {
        Optional<UserEntity> userByEmail = userJpaRepository.findByEmail(email);

        return userByEmail.map(userEntity -> UserPortResponse.builder()
            .userId(userEntity.getUserId())
            .password(userEntity.getPassword())
            .username(userEntity.getUsername())
            .email(userEntity.getEmail())
            .phone(userEntity.getPhone())
            .build());
    }

    @Override
    @Transactional
    public Optional<UserPortResponse> findByProviderId(String providerId) {
        Optional<SocialUserEntity> userByProviderId = socialUserJpaRepository.findByProviderId(providerId);

        if (userByProviderId.isEmpty()) {
            return Optional.empty();
        }

        SocialUserEntity socialUserEntity = userByProviderId.get();
        Optional<UserSubscription> userBySocialUserId = userSubscriptionRepository.findByUserId(socialUserEntity.getSocialUserId());

        String role = userBySocialUserId
            .map(subscription -> subscription.getSubscriptionType().getRole())
            .orElse(SubscriptionType.FREE.getRole());

        return Optional.of(UserPortResponse.builder()
            .userId(socialUserEntity.getSocialUserId())
            .username(socialUserEntity.getUsername())
            .provider(socialUserEntity.getProvider())
            .providerId(socialUserEntity.getProviderId())
            .role(role)
            .build());
    }

    @Override
    public Optional<UserPortResponse> findByUserId(String userId) {
        Optional<UserEntity> userByUserId = userJpaRepository.findByUserId(userId);

        return userByUserId.map(userEntity -> UserPortResponse.builder()
            .userId(userEntity.getUserId())
            .password(userEntity.getPassword())
            .username(userEntity.getUsername())
            .email(userEntity.getEmail())
            .phone(userEntity.getPhone())
            .build());
    }

    @Override
    @Transactional
    public UserPortResponse create(CreateUser createUser) {
        UserEntity userEntity = new UserEntity(
            createUser.username(),
            createUser.encryptedPassword(),
            createUser.email(),
            createUser.phone()
        );

        UserEntity savedUserEntity = userJpaRepository.save(userEntity);
        userSubscriptionRepository.create(savedUserEntity.getUserId());

        return UserPortResponse.builder()
            .userId(savedUserEntity.getUserId())
            .username(savedUserEntity.getUsername())
            .password(savedUserEntity.getPassword())
            .email(savedUserEntity.getEmail())
            .phone(savedUserEntity.getPhone())
            .build();
    }

    @Override
    @Transactional
    public UserPortResponse createSocialUser(String username, String provider, String providerId) {
        SocialUserEntity socialUserEntity = new SocialUserEntity(username, provider, providerId);
        socialUserJpaRepository.save(socialUserEntity);
        userSubscriptionRepository.create(socialUserEntity.getSocialUserId());

        return UserPortResponse.builder()
            .username(socialUserEntity.getUsername())
            .provider(socialUserEntity.getProvider())
            .providerId(socialUserEntity.getProviderId())
            .build();
    }


    @Override
    @Transactional
    public void createHistory(CreateUserHistory createUserHistory) {
        UserHistoryEntity userHistoryEntity = new UserHistoryEntity(
            createUserHistory.userId(),
            createUserHistory.userRole(),
            createUserHistory.clientIp(),
            createUserHistory.reqMethod(),
            createUserHistory.reqUrl(),
            createUserHistory.reqHeader(),
            createUserHistory.reqPayload()
        );

        userHistoryJpaRepository.save(userHistoryEntity);
    }
}
