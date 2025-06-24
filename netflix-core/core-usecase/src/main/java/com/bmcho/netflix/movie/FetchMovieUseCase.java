package com.bmcho.netflix.movie;


import com.bmcho.netflix.movie.response.PageableMoviesResponse;

public interface FetchMovieUseCase {
    PageableMoviesResponse fetchFromClient(int page);
}
