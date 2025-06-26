package com.bmcho.netflix.respository.user;

import com.bmcho.netfilx.user.CreateUser;
import com.bmcho.netfilx.user.FetchUserPort;
import com.bmcho.netfilx.user.InsertUserPort;
import com.bmcho.netfilx.user.UserPortResponse;
import com.bmcho.netflix.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository implements InsertUserPort, FetchUserPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserPortResponse> findByEmail(String email) {
        Optional<UserEntity> userByEmail = userJpaRepository.findByEmail(email);
        return userByEmail.map(userEntity -> UserPortResponse.builder()
            .userId(userEntity.getUserId())
            .password(userEntity.getPassword())
            .userName(userEntity.getUsername())
            .email(userEntity.getEmail())
            .phone(userEntity.getPhone())
            .build());
    }

    @Override
    public UserPortResponse create(CreateUser createUser) {
        UserEntity userEntity = new UserEntity(
            createUser.getUsername(),
            createUser.getEncryptedPassword(),
            createUser.getEmail(),
            createUser.getPhone()
        );

        UserEntity savedUserEntity = userJpaRepository.save(userEntity);
        return UserPortResponse.builder()
            .userId(savedUserEntity.getUserId())
            .userName(savedUserEntity.getUsername())
            .password(savedUserEntity.getPassword())
            .email(savedUserEntity.getEmail())
            .phone(savedUserEntity.getPhone())
            .build();
    }
}
