package com.bmcho.netflix.respository.token;

import com.bmcho.netflix.entity.token.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, String>, TokenCustomRepository {
}
