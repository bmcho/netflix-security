package com.bmcho.netflix.movie;

import com.bmcho.netflix.movie.response.MovieResponse;

import java.util.List;

public interface InsertMovieUseCase {
    void insert(List<MovieResponse> movies);
}
