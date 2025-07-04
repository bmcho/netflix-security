package com.bmcho.netflix.movie;


import com.bmcho.netflix.movie.response.PageableMoviesBatchResponse;
import com.bmcho.netflix.movie.response.PageableMoviesResponse;

public interface FetchMovieUseCase {
    PageableMoviesBatchResponse fetchFromClient(int page);

    PageableMoviesResponse fetchFromDb(int page);
}
