package com.bmcho.netflix.respository.movie.movie;


import com.bmcho.netflix.entity.movie.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieJpaRepository extends JpaRepository<MovieEntity, String>, MovieCustomRepository {
    Optional<MovieEntity> findByMovieId(String movieId);
}
