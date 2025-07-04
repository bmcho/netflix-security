package com.bmcho.netfilx.movie;

import com.bmcho.netflix.movie.UserMovieLike;

import java.util.Optional;

public interface LikeMoviePort {
    void save(UserMovieLike userMovieLike);

    Optional<UserMovieLike> findByUserIdAndMovieId(String userId, String movieId);
}
