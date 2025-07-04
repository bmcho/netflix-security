package com.bmcho.netflix.respository.user;

import com.bmcho.netflix.entity.user.UserHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoryJpaRepository extends JpaRepository<UserHistoryEntity, Long> {
}