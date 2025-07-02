package com.bmcho.netflix.respository.user;

import com.bmcho.netflix.entity.user.SocialUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialUserJpaRepository extends JpaRepository<SocialUserEntity, String> {
    Optional<SocialUserEntity> findByProviderId(String providerId);
}
