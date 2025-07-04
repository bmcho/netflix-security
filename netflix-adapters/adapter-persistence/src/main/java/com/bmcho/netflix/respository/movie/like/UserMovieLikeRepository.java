package com.bmcho.netflix.respository.movie.like;

import com.bmcho.netfilx.movie.LikeMoviePort;
import com.bmcho.netflix.entity.movie.UserMovieLikeEntity;
import com.bmcho.netflix.movie.UserMovieLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserMovieLikeRepository implements LikeMoviePort {
    private final UserMovieLikeJpaRepository userMovieLikeJpaRepository;

    @Override
    @Transactional
    public Optional<UserMovieLike> findByUserIdAndMovieId(String userId, String movieId) {
        return userMovieLikeJpaRepository.findByUserIdAndMovieId(userId, movieId)
                .map(UserMovieLikeEntity::toDomain);
    }

    @Override
    @Transactional
    public void save(UserMovieLike userMovieLike) {
        UserMovieLikeEntity userMovieLikeEntity = UserMovieLikeEntity.toEntity(userMovieLike);
        userMovieLikeJpaRepository.save(userMovieLikeEntity);
    }
}
