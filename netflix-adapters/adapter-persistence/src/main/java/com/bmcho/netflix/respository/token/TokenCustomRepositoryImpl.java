package com.bmcho.netflix.respository.token;

import com.bmcho.netflix.entity.token.QTokenEntity;
import com.bmcho.netflix.entity.token.TokenEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.bmcho.netflix.entity.token.QTokenEntity.tokenEntity;

@Repository
@RequiredArgsConstructor
public class TokenCustomRepositoryImpl implements TokenCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TokenEntity> findByUserId(String userId) {
        // QTokenEntity 생성을 위해 코드 작성 전에 build를 한번 진행해 줄것
        return jpaQueryFactory.selectFrom(tokenEntity)
                .where(tokenEntity.userId.eq(userId))
                .fetch()
                .stream().findFirst();
    }
}
